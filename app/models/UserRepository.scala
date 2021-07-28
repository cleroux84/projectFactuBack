package models

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.BillService
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with BillService {

  import profile.api._

  def getUserList: Future[Seq[User]] = {
    db.run(slickUser.result)
  }

  def getUser(id: Long): Future[User] = {
    val query = slickUser.filter(_.id === id)
    db.run(query.result.head)
  }
}
