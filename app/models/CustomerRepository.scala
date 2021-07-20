package models
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.BillService
import slick.jdbc.JdbcProfile

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
    db.run(slickCustomer += newCustomer).map(res => "Customer successfully created")
    }

  def updateCustomer(id: Long, civility: String, firstName: String, lastName: String, email: String, phone: String, phone2: Option[String], company: Option[String], address: String, city: String, zipCode: String, VATNumber: String): Future[Int] = {
    db.run(slickCustomer
    .filter(_.id === id)
    .map(x=>(x.civility, x.firstName, x.lastName, x.email, x.phone, x.phone2, x.company, x.address, x.zipCode, x.city, x.VATNumber))
    .update(civility, firstName, lastName, email, phone, phone2, company, address, zipCode, city, VATNumber))
  }

}
