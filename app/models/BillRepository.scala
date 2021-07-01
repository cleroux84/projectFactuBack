package models

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import com.github.tototoshi.slick.MySQLJodaSupport._
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{Await, ExecutionContext, Future}
import models._

import java.awt.Cursor
import java.text.DecimalFormat
import scala.concurrent.duration.DurationInt
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

  import dbConfig._
  import profile.api._

   class BillTable(tag: Tag) extends Table[Bill](tag, "bill") {
      def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
      def customerId = column[Long]("customerId")
//      def created = column[DateTime]("created")
      def periodCovered = column[String]("periodCovered")
      def billNumber = column[String]("billNumber")
      def benefit = column[String]("benefit")
      def quantity = column[Int]("quantity")
      def unitPrice = column[Int]("unitPrice")
      def vatRate = column[Int]("vatRate")

    def * =  (id, customerId, /*created,*/ periodCovered, billNumber, benefit, quantity, unitPrice, vatRate) <> ((Bill.apply _).tupled, Bill.unapply)
  }

  val slickBill = TableQuery[BillTable]

  def composeBillNumber(): String = {
    val date = DateTime.now().toString()
    val year = date.split("-")(0)
    val q = slickBill.sortBy(_.id.reverse).map(_.billNumber).result.headOption
    val lastBillNumber = db.run(q).map(lastBillOpt => lastBillOpt.map(_.split("-")(1)))
    val lastBillYear = db.run(q).map(lastBillOpt => lastBillOpt.map(_.split("-")(0)))
    val billExist = Await.result(db.run(slickBill.result), 1.seconds)
    billExist match {
      case x if billExist.isEmpty =>
        val result = year + "-" + "001"
        result
      case _ =>
        val newBillYear = Await.result(lastBillYear, 1 seconds).head
        newBillYear match {
          case x if year == newBillYear =>
            val newBillNumber = Await.result(lastBillNumber, 1 seconds).head.toInt + 1
            val formatNumber = new DecimalFormat("000")
            val result = year + "-" + formatNumber.format(newBillNumber)
            result
          case _ =>
            val result = year + "-" + "001"
            result
        }
    }
  }

  def getListBill: Future[Seq[BillWithCustomerData]] = {
//    val q = slickBill.sortBy(_.id.reverse).map(_.billNumber).result.headOption
//    val lastBillNumber = db.run(q).map(lastBillOpt => lastBillOpt.map(_.split("-")(1))).map(x => x.map(y => println(y)))

    // 1
//    db.run(q.result.headOption).map(x => println(x))

    // 2
//    db.run(q.result).map { x =>
//      val toto = x.map(_.billNumber)
//      println(toto)
//      toto
//    }


    val query = slickBill.join(customerInstance.slickCustomer).on(_.customerId === _.id)
//    val t: Future[Seq[(Bill, Customer)]] = db.run(query.result)
    db.run(query.result).map { billAndCustomerSeq =>
//      val q: Seq[(Bill, Customer)] = billAndCustomerSeq
      billAndCustomerSeq.map { billAndCust =>
        val r: (Bill, Customer) = billAndCust
        BillWithCustomerData.fromBillAndCustomerTables(billAndCust._1, billAndCust._2)
      }
    }
  }

  def deleteBill(id: Long): Future[Int] = {db.run(
    slickBill.filter(_.id === id).delete)
  }

  def addBill(newBill: Bill): Future[String] = {
//    println(newBill)
    db.run(slickBill += newBill).map(res => "Bill successfully created")
  }

}