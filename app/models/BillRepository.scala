package models

import org.joda.time.DateTime
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import com.github.tototoshi.slick.MySQLJodaSupport._
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{Await, ExecutionContext, Future}
import services.BillService

import java.text.DecimalFormat
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

@Singleton
class BillRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with BillService {
  val customerInstance: CustomerRepository = new CustomerRepository(dbConfigProvider)
  val benefitInstance: BenefitRepository = new BenefitRepository(dbConfigProvider)

  import profile.api._

  def getListBillNumber(): Future[Option[String]] = {
    val query = slickBill.sortBy(_.id.reverse).map(_.billNumber).result.headOption
    db.run(query)
  }

  def composeBillNumber(): Future[String] = {
    val date = DateTime.now().toString()
    val year = date.split("-")(0)

    this. getListBillNumber().map { billNumberOpt =>
      val lastBillNumber = billNumberOpt.map(_.split("-")(1)).getOrElse("000")
      val lastBillYear = billNumberOpt.map(_.split("-").head).getOrElse(year)
      val formatNumber = new DecimalFormat("000")

      lastBillYear match {
        case y if y == year => s"$y-${formatNumber.format(lastBillNumber.toInt + 1)}"
        case _ => s"$year-001"
      }
    }
  }

  def getBillWithData(billCustomerSeq: Seq[(Bill, Customer)]): Future[Seq[BillWithData]] = {

    Future.sequence(billCustomerSeq.map { billCustomerBenefit =>
      benefitInstance.getListBenefit(billCustomerBenefit._1).map { benefitSeq =>
        val benefitWithAmount = benefitSeq.map { x =>
          BenefitWithMount.fromBenefitToAmounts(x)
        }
        val totalHT = benefitWithAmount.map(_.amountHt).sum.setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
        val seqHt = benefitWithAmount.map { benef => benef.amountHt * (1 + (benef.vatRate/100))}
        val totalTtc = seqHt.sum.setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
        BillWithData.fromBillAndCustomerTables(billCustomerBenefit._1, billCustomerBenefit._2, benefitWithAmount, totalHT, totalTtc)
      }
    })
  }

  def getListBill: Future[Seq[BillWithData]] = {
    val query = slickBill
      .join(slickCustomer).on(_.customerId === _.id)
    db.run(query.result).flatMap { billCustomerSeq =>
      this.getBillWithData(billCustomerSeq).map(_.sortBy(_.billNumber).reverse)
    }
  }

  def getListBillByUser(userId: Long): Future[Seq[BillWithData]] = {
    val query = slickBill
      .filter(_.userId === userId)
      .join(slickCustomer).on(_.customerId === _.id)
    db.run(query.result).flatMap { billCustomerSeq =>
      this.getBillWithData(billCustomerSeq).map(_.sortBy(_.billNumber).reverse)
    }
  }

  def findBill(id: Long): Future[Seq[BillWithData]] = {
    val q = slickBill.filter(_.id === id)
      .join(slickCustomer).on(_.customerId === _.id)
    db.run(q.result).flatMap { x =>
      this.getBillWithData(x)
    }
  }

  def addBill(newBill: Bill): Future[Long] = {
    db.run(slickBill returning slickBill.map(_.id) += newBill)
  }

  //  def deleteBill(id: Long): Future[Int] = {db.run(
  //    slickBill.filter(_.id === id).delete)
  //  }

}




// Mieux pour grosse db : à revoir et appliquer :
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