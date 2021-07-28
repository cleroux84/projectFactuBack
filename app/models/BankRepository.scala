package models

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.BillService
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BankRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with BillService {
  import profile.api._

//  def addBank(bank: Bank): Future[String] = {
//    db.run(slickBank += bank).map(res => "Bank successfully created")
//  }

  def getBank(user: User): Future[Option[Bank]] = {
    val query = slickBank.filter(_.id === user.id )
    db.run(query.result.headOption)
  }
}
