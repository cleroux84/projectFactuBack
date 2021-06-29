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
   val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

   class CustomerTable(tag: Tag) extends Table[Customer](tag, "customer") {
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
      def VATNumber = column[String]("VATNumber")

    def * =  (id, civility, firstName, lastName, email, phone, phone2, address, city, zipCode, company, VATNumber) <> ((Customer.apply _).tupled, Customer.unapply)
  }
  val slickCustomer = TableQuery[CustomerTable]

  //renommer les TableQuery + separer DAO

  def getList: Future[Seq[Customer]] = {
  db.run(slickCustomer.result)
}

  def deleteCustomer(id: Long): Future[Int] = db.run {
    slickCustomer.filter(_.id === id).delete
  }

  def addCustomer(newCustomer: Customer): Future[String] = {
    db.run(slickCustomer += newCustomer).map(res => "Customer successfully created")
    }

  def updateCustomer(id: Long, civility: String, firstName: String, lastName: String, email: String, phone: String, phone2: Option[String], company: Option[String], address: String, city: String, zipCode: String, VATNumber: String): Future[Int] = {
    db.run(slickCustomer
    .filter(_.id === id)
    .map(x=>(x.civility, x.firstName, x.lastName, x.email, x.phone, x.phone2, x.company, x.address, x.zipCode, x.city, x.VATNumber))
    .update(civility, firstName, lastName, email, phone, phone2, company, address, zipCode, city, VATNumber))
  }

}
//
//
//package models
//import javax.inject.{Inject, Singleton}
//import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
//import slick.jdbc.JdbcProfile
//
//import scala.concurrent.ExecutionContext.Implicits.global
//
//import scala.concurrent.{ExecutionContext, Future}
///**
// * A repository for people.
// *
// * @param dbConfigProvider The Play db config provider. Play will inject this for you.
// */
//@Singleton
//trait CustomerRepository extends HasDatabaseConfigProvider[JdbcProfile] with BillRepo {
//  //  private val dbConfig = dbConfigProvider.get[JdbcProfile]
//
//  import dbConfig._
//  import profile.api._
//
//  private class CustomerTable(tag: Tag) extends Table[Customer](tag, "customer") {
//    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
//    def civility = column[String]("civility")
//    def firstName = column[String]("firstName")
//    def lastName = column[String]("lastName")
//    def email = column[String]("email")
//    def phone = column[String]("phone")
//    def phone2 = column[Option[String]]("phone2")
//    def address = column[String]("address")
//    def city = column[String]("city")
//    def zipCode = column[String]("zipCode")
//    def company = column[Option[String]]("company")
//    def VATNumber = column[String]("VATNumber")
//
//    def * =  (id, civility, firstName, lastName, email, phone, phone2, address, city, zipCode, company, VATNumber) <> ((Customer.apply _).tupled, Customer.unapply)
//  }
//  val customer = TableQuery[CustomerTable]
//
//  def getList: Future[Seq[Customer]] = {
//    db.run(customer.result)
//  }
//
//  def deleteCustomer(id: Long): Future[Int] = db.run {
//    customer.filter(_.id === id).delete
//  }
//
//  def addCustomer(newCustomer: Customer): Future[String] = {
//    db.run(customer += newCustomer).map(res => "Customer successfully created")
//  }
//
//  def toto = customer.join(bill)
//
//  def updateCustomer(id: Long, civility: String, firstName: String, lastName: String, email: String, phone: String, phone2: Option[String], company: Option[String], address: String, city: String, zipCode: String, VATNumber: String): Future[Int] = {
//    db.run(customer
//      .filter(_.id === id)
//      .map(x=>(x.civility, x.firstName, x.lastName, x.email, x.phone, x.phone2, x.company, x.address, x.zipCode, x.city, x.VATNumber))
//      .update(civility, firstName, lastName, email, phone, phone2, company, address, zipCode, city, VATNumber))
//  }
//
//}