package models

import controllers.CustomerController
import javax.inject.Inject
import play.api.libs.ws._
import play.api.libs.ws.ahc.AhcWSClient
import play.api.data.Form
import play.api.data.Forms.{email, mapping, nonEmptyText, optional, text}
import play.api.libs.json.Json
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.ws.WSClient
import slick.jdbc.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._

//case class CustomerId(value : String)
//
//object CustomerId {
//  implicit val customerIdFormat = Json.format[CustomerId]
//}

case class Customer(
                     id: Long,
                     civility: String,
                     firstName : String,
                     lastName: String,
                     email: String,
                     phone: String,
                     phone2: Option[String],
                     address: String,
                     city: String,
                     zipCode: String,
                     company: Option[String]
                   )
object Customer {
  implicit val customerFormat = Json.format[Customer]
}

//case class CustomerFormData (
//                              civility: String,
//                              firstName : String,
//                              lastName: String,
//                              mail: String,
//                              phone: String,
//                              phone2: Option[String],
//                              address: String,
//                              city: String,
//                              zipCode: String,
//                              company: Option[String]
//                            )
//
//object CustomerForm {
//  val form = Form(
//    mapping(
//      "civility" -> nonEmptyText,
//      "firstName" -> nonEmptyText,
//      "lastName" -> nonEmptyText,
//      "mail" -> email,
//      "phone" -> nonEmptyText,
//      "phone2" -> optional(text),
//      "address" -> nonEmptyText,
//      "city" -> nonEmptyText,
//      "zipCode" -> nonEmptyText,
//      "company" -> optional(text)
//    )(CustomerFormData.apply)(CustomerFormData.unapply)
//  )
//}

//class CustomerTableDef(tag: Tag) extends Table[Customer](tag, "customer") {
//  def civility = column[String]("civility")
//  def fistName = column[String]("fistName")
//  def lastName = column[String]("lastName")
//  def mail = column[String]("email")
//  def phone = column[String]("phone")
//  def phone2 = column[Option[String]]("phone2")
//  def address = column[String]("address")
//  def city = column[String]("city")
//  def zipCode = column[String]("zipCode")
//  def company = column[Option[String]]("company")
//
//  override def * =
//    (civility, fistName, lastName, mail, phone, phone2, address, city, zipCode, company)<>(Customer.tupled, Customer.unapply)
//}

//class Customers {
////  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
//  //@inject (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]
//  var customers: Seq[Customer] = Seq()
//
//  def listAll: Future[Option[Customer]] = {
//    ???
////    dbConfig.db.run(customers.result)
//  }
//}
