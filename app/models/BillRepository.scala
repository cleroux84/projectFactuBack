package models

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import com.github.tototoshi.slick.MySQLJodaSupport._
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class BillRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class BillTable(tag: Tag) extends Table[Bill](tag, "bill") {
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
  private val bill = TableQuery[BillTable]

  def getListBill: Future[Seq[Bill]] = {
    dbConfig.db.run(bill.result)
  }
}
