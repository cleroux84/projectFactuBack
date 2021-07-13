package models
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.Json
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


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
    def name = column[String]("name")
    def quantity = column[BigDecimal]("quantity")
    def unitPrice = column[BigDecimal]("unitPrice")
    def vatRate = column[BigDecimal]("vatRate")

    def * = (id, billId, name, quantity, unitPrice, vatRate) <> ((Benefit.apply _).tupled, Benefit.unapply)
  }
val slickBenefit: TableQuery[BenefitTable] = TableQuery[BenefitTable]

  case class CreateBenefitForm(
                                billId: Long,
                                name: String,
                                quantity: BigDecimal,
                                unitPrice: BigDecimal,
                                vatRate: BigDecimal
                              ){
    def toBenefitCustom(billId: Long): Benefit = Benefit(
      id = 0L,
      billId = billId,
      name = this.name,
      quantity = this.quantity,
      unitPrice = this.unitPrice,
      vatRate = this.vatRate
    )
  }

  object CreateBenefitForm {
    implicit val reader = Json.reads[CreateBenefitForm]
  }


  def addBenefit(benefits: Benefit) = {
        println(benefits)
//    benefits.map{benefit => db.run(slickBenefit += benefit).map(res => "Benefit success")}
    db.run(slickBenefit += benefits).map(res => "Benefit successfully created")
//    db.run(slickBenefit returning slickBenefit.map(_.id) ++= benefits)
  }


//def addBenefit(bill: Bill, benefits: Seq[Benefit]): Future[String] = {
//  db.run(
//  slickBenefit.returning(slickBenefit.map(_.billId)) ++= benefits.map(benefit =>
//    Benefit(id = 0L,
//      billId = bill.id,
//      name = benefit.name,
//      quantity = benefit.quantity,
//      unitPrice = benefit.unitPrice,
//      vatRate = benefit.vatRate)))
////  db.run(slickBenefit ++= benefits).map(res => "Benefit successfully created")
//}

}

