package models
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class CustomerRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class CustomerTable(tag: Tag) extends Table[Customer](tag, "customer") {
      def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
      def civility = column[String]("civility")
      def firstName = column[String]("firstName")
      def lastName = column[String]("lastName")
      def email = column[String]("email")
      def phone = column[String]("phone")
      def phone2 = column[Option[String]]("phone2")
      def address = column[String]("address")
      def city = column[String]("city")
      def zipCode = column[String]("zipCode")
      def company = column[Option[String]]("company")

    def * =  (id, civility, firstName, lastName, email, phone, phone2, address, city, zipCode, company) <> ((Customer.apply _).tupled, Customer.unapply)
  }
  private val customer = TableQuery[CustomerTable]

  def getList(): Future[Seq[Customer]] = db.run {
    customer.result
  }
}
