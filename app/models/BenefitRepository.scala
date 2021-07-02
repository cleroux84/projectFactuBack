package models
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext


/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class BenefitRepository @Inject()(dbConfigProvider: DatabaseConfigProvider) (implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class BenefitTable(tag: Tag) extends Table[Benefit](tag, "benefit") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def billId= column[Long]("billId")
    def periodCovered = column[String]("periodCovered")
    def name = column[String]("name")
    def quantity = column[BigDecimal]("quantity")
    def unitPrice = column[BigDecimal]("unitPrice")
    def vatRate = column[BigDecimal]("vatRate")

    def * = (id, billId, periodCovered, name, quantity, unitPrice, vatRate) <> ((Benefit.apply _).tupled, Benefit.unapply)
  }
val slickBenefit: TableQuery[BenefitTable] = TableQuery[BenefitTable]
}
