package models

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import com.github.tototoshi.slick.MySQLJodaSupport._
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{Await, ExecutionContext, Future}
import models._

import java.text.DecimalFormat
import scala.language.postfixOps

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class BillRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val customerInstance: CustomerRepository = new CustomerRepository(dbConfigProvider)
  val benefitInstance: BenefitRepository = new BenefitRepository(dbConfigProvider)

  import dbConfig._
  import profile.api._

   class BillTable(tag: Tag) extends Table[Bill](tag, "bill") {
      def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
      def customerId = column[Long]("customerId")
//      def created = column[DateTime]("created")
      def periodCovered = column[String]("periodCovered")
      def billNumber = column[String]("billNumber")
      def benefit = column[String]("benefit")
      def quantity = column[BigDecimal]("quantity")
      def unitPrice = column[BigDecimal]("unitPrice")
      def vatRate = column[BigDecimal]("vatRate")

    def * =  (id, customerId, /*created,*/ periodCovered, billNumber, benefit, quantity, unitPrice, vatRate) <> ((Bill.apply _).tupled, Bill.unapply)
  }

  val slickBill = TableQuery[BillTable]
//  val slickBenefit = TableQuery[BenefitTable]

  def getListBillNumber(): Future[Option[String]] = {
    val query = slickBill.sortBy(_.id.reverse).map(_.billNumber).result.headOption
    db.run(query)
  }

  def composeBillNumber(): Future[String] = {
    val date = DateTime.now().toString()
    val year = date.split("-")(0)

    this.getListBillNumber().map { billNumberOpt =>
      val lastBillNumber = billNumberOpt.map(_.split("-")(1)).getOrElse("000")
      val lastBillYear = billNumberOpt.map(_.split("-").head).getOrElse(year)
      val formatNumber = new DecimalFormat("000")

      lastBillYear match {
        case y if y == year => s"$y-${formatNumber.format(lastBillNumber.toInt + 1)}"
        case _ => s"$year-001"
      }
    }
  }
//ce qye j'avais fait au depart et qui fonctionnait :
//  def composeBillNumber(): String = {
//    val date = DateTime.now().toString()
//    val year = date.split("-")(0)
//    val q = slickBill.sortBy(_.id.reverse).map(_.billNumber).result.headOption
//    val lastBillNumber = db.run(q).map(lastBillOpt => lastBillOpt.map(_.split("-")(1)))
//    val lastBillYear = db.run(q).map(lastBillOpt => lastBillOpt.map(_.split("-")(0)))
//    val billExist = Await.result(db.run(slickBill.result), 1.seconds)
//    billExist match {
//      case x if billExist.isEmpty =>
//        val result = year + "-" + "001"
//        result
//      case _ =>
//        val newBillYear = Await.result(lastBillYear, 1 seconds).head
//        newBillYear match {
//          case x if year == newBillYear =>
//            val newBillNumber = Await.result(lastBillNumber, 1 seconds).head.toInt + 1
//            val formatNumber = new DecimalFormat("000")
//            val result = year + "-" + formatNumber.format(newBillNumber)
//            result
//          case _ =>
//            val result = year + "-" + "001"
//            result
//        }
//    }
//  }

  //fait avant de creer la table benefit :
//  def getListBill: Future[Seq[BillWithData]] = {
//    val query = slickBill
//      .join(customerInstance.slickCustomer).on(_.customerId === _.id)
//      .join(benefitInstance.slickBenefit).on(_._1.id === _.billId)
//    db.run(query.result).map { billCustomerBenefitSeq =>
//      billCustomerBenefitSeq.map { billCustomerBenefit =>
//        BillWithData.fromBillAndCustomerTables(billCustomerBenefit._1._1, billCustomerBenefit._1._2, billCustomerBenefit._2)
//      }
//    }
//  }

  def getListBenefit(bill: Bill): Future[Seq[Benefit]] = {
    val q = benefitInstance.slickBenefit
      .filter(_.billId ===bill.id)
   db.run(q.result)
  }

  def getListBill: Future[Seq[BillWithData]] = {
    val query = slickBill
      .join(customerInstance.slickCustomer).on(_.customerId === _.id)

    db.run(query.result).flatMap { billCustomerSeq =>
      Future.sequence(billCustomerSeq.map { billCustomerBenefit =>
        getListBenefit(billCustomerBenefit._1).map { benefitSeq =>
          BillWithData.fromBillAndCustomerTables(billCustomerBenefit._1, billCustomerBenefit._2, benefitSeq)
        }
      })
    }
  }

// Mieux pour gros db : à revoir et appliquer :
//  def getListBill2: Future[Seq[BillWithData]] = {
//    val query = slickBill
//      .join(customerInstance.slickCustomer).on(_.customerId === _.id)
//      .join(benefitInstance.slickBenefit).on(_._1.id === _.billId)
//
//    db.run(query.result).map { x =>
//      val t: Seq[((Bill, Customer), Benefit)] = x
//
////      x.groupBy(_._1).map { seqTuple =>
////        BillWithData.fromBillAndCustomerTables(seqTuple._1._1, seqTuple._1._2, seqTuple._2.map(_._2))
//      x.groupBy(_._1).map { case ((bill, client), seqDuReste) =>
//        BillWithData.fromBillAndCustomerTables(bill, client, seqDuReste.map(_._2))
////        val p: ((Bill, Customer), Seq[((Bill, Customer), Benefit)]) = seqTuple
//
//        // ((Bill 1, Client1), B1)
//        // ((Bill 1, Client1), B1)
//        // ((Bill 2, Client1), B1)
//        // ((Bill 2, Client1), B2)
//
//        // Suite au groupBy => Map
//        //          Key                                 Values
//        // ((Bill 1, Client1), Seq(((Bill 1, Client1), B1),  ((Bill 1, Client1), B2))
//        // ((Bill 2, Client1), Seq(((Bill 2, Client1), B1)), ((Bill 2, Client1), B2))
//
//
//      }.toSeq
//    }

//    db.run(query.result).flatMap { billCustomerSeq =>
//      Future.sequence(billCustomerSeq.map { billCustomerBenefit =>
//        getListBenefit(billCustomerBenefit._1).map { benefitSeq =>
//          BillWithData.fromBillAndCustomerTables(billCustomerBenefit._1, billCustomerBenefit._2, benefitSeq)
//        }
//      })
//    }
//  }


  def deleteBill(id: Long): Future[Int] = {db.run(
    slickBill.filter(_.id === id).delete)
  }

  def addBill(newBill: Bill): Future[String] = {
    println(newBill)
    db.run(slickBill += newBill).map(res => "Bill successfully created")
  }
}