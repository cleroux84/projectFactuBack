package repositories

import models.Customer
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.BillService
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CustomerRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with BillService {

  import profile.api._

  def getList: Future[Seq[Customer]] = {
  db.run(slickCustomer.result)
}

  def deleteCustomer(id: Long): Future[Int] = db.run {
    slickCustomer.filter(_.id === id).delete
  }

  def addCustomer(newCustomer: Customer): Future[String] = {
    val phone = newCustomer.phone.filter(Character.isDigit).replaceAll("..(?!$)", "$0 ")
    val phone2 = Option(newCustomer.phone2.getOrElse("").filter(Character.isDigit).replaceAll("..(?!$)", "$0 "))
    db.run(slickCustomer += Customer
    (
      id = newCustomer.id,
      civility = newCustomer.civility,
      firstName = newCustomer.firstName.split(' ').map(_.capitalize).mkString(" "),
      lastName = newCustomer.lastName.split(' ').map(_.capitalize).mkString(" "),
      email = newCustomer.email,
      phone = phone,
      phone2 = phone2,
      address = newCustomer.address,
      city = newCustomer.city,
      zipCode = newCustomer.zipCode,
      company = Option(newCustomer.company.getOrElse("").split(' ').map(_.capitalize).mkString(" ")),
      VATNumber = newCustomer.VATNumber
    )
    ).map(res => "Customer successfully created")
    }

  def updateCustomer(id: Long, civility: String, firstName: String, lastName: String, email: String, phone: String, phone2: Option[String], company: Option[String], address: String, city: String, zipCode: String, VATNumber: String): Future[Int] = {
    db.run(slickCustomer
    .filter(_.id === id)
    .map(x=>(x.civility, x.firstName, x.lastName, x.email, x.phone, x.phone2, x.company, x.address, x.zipCode, x.city, x.VATNumber))
    .update(civility, firstName, lastName, email, phone, phone2, company, address, zipCode, city, VATNumber))
  }

}
