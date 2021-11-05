package services

import com.github.tototoshi.slick.MySQLJodaSupport._
import models.{Bank, Benefit, Bill, Customer, User}
import org.joda.time.DateTime
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait BillService extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  //DESIGN PATTERN LAZY INITIALIZATION ?? https://pavelfatin.com/design-patterns-in-scala/
  lazy val slickBill = TableQuery[BillTable]
  lazy val slickCustomer = TableQuery[CustomerTable]
  lazy val slickBenefit = TableQuery[BenefitTable]
  lazy val slickUser = TableQuery[UserTable]
  lazy val slickBank = TableQuery[BankTable]

  class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def civility = column[String]("civility")
    def firstName = column[String]("firstName")
    def lastName = column[String]("lastName")
    def email = column[String]("email")
    def phone = column[String]("phone")
    def address = column[String]("address")
    def city = column[String]("city")
    def zipCode = column[String]("zipCode")
    def siret = column[String]("siret")
    def role = column[Int]("role")
    def authId = column[String]("authId")

    def * = (id, civility, firstName, lastName, email, phone, address, city, zipCode, siret, role, authId) <> ((User.apply _).tupled, User.unapply)

  }

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
    def created = column[DateTime]("created")
    def periodCovered = column[String]("periodCovered")
    def billNumber = column[String]("billNumber")
    def userId = column[Long]("userId")
    def paid = column[Boolean]("paid")
    def paymentDate = column[Option[DateTime]]("paymentDate")

    def * = (id, customerId, created, periodCovered, billNumber, userId, paid, paymentDate) <> ((Bill.apply _).tupled, Bill.unapply)
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

  class BankTable(tag: Tag) extends Table[Bank](tag, "bank") {
    def id= column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def bankCode = column[String]("bankCode")
    def guichetCode = column[String]("guichetCode")
    def account = column[String]("account")
    def ribKey = column[BigDecimal]("ribKey")
    def iban = column[String]("iban")
    def userId = column[Long]("userId")

    def * = (id, name, bankCode, guichetCode, account, ribKey, iban, userId) <> ((Bank.apply _).tupled, Bank.unapply)
  }

}
