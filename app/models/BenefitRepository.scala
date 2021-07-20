package models
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{Json, Reads}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BenefitRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with BillService {
  import profile.api._

  def addBenefit(benefits: Seq[Benefit]): Future[String] = {
    println(benefits)
    db.run(slickBenefit ++= benefits).map(res => "Benefit successfully created")
  }

  def getListBenefit(bill: Bill): Future[Seq[Benefit]] = {
    val q = slickBenefit.filter(_.billId ===bill.id)
    db.run(q.result)
  }
}

