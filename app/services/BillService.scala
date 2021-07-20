package services

import models.{Benefit, Bill, Customer}
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait BillService extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val slickBill: TableQuery[BillTable] = TableQuery[BillTable]
  val slickCustomer: TableQuery[CustomerTable] = TableQuery[CustomerTable]
  val slickBenefit: TableQuery[BenefitTable] = TableQuery[BenefitTable]

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

    def * = (id, civility, firstName, lastName, email, phone, phone2, address, city, zipCode, company, VATNumber) <> ((Customer.apply _).tupled, Customer.unapply)
  }

  class BillTable(tag: Tag) extends Table[Bill](tag, "bill") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def customerId = column[Long]("customerId")
    //      def created = column[DateTime]("created")
    def periodCovered = column[String]("periodCovered")
    def billNumber = column[String]("billNumber")

    def * = (id, customerId, /*created,*/ periodCovered, billNumber) <> ((Bill.apply _).tupled, Bill.unapply)
  }

  class BenefitTable(tag: Tag) extends Table[Benefit](tag, "benefit") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def billId = column[Long]("billId")
    def name = column[String]("name")
    def quantity = column[BigDecimal]("quantity")
    def unitPrice = column[BigDecimal]("unitPrice")
    def vatRate = column[BigDecimal]("vatRate")

    def * = (id, billId, name, quantity, unitPrice, vatRate) <> ((Benefit.apply _).tupled, Benefit.unapply)
  }

}
