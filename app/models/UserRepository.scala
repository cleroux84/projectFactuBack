package models

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.BillService
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with BillService {
  val bankInstance: BankRepository = new BankRepository(dbConfigProvider)

  import profile.api._

  def getUserList: Future[Seq[User]] = {
    db.run(slickUser.result)
  }

  def getUser(email: String): Future[User] = {
    val query = slickUser.filter(_.email === email)
    db.run(query.result.head)
  }

  def getUserWithBank(email: String): Future[UserWithBank] = {
    getUser(email).map { user =>
      bankInstance.getBank(user).map { bankOption =>
        UserWithBank.fromUserToBank(user, bankOption)
      }
    }.flatten
  }

  def addUser(newUser: User): Future[Int] = {
    db.run(slickUser += User
    (
      id = newUser.id,
      civility = newUser.civility,
      firstName = newUser.firstName,
      lastName = newUser.lastName,
      email = newUser.email,
      phone = newUser.phone,
      address = newUser.address,
      city = newUser.city,
      zipCode = newUser.zipCode,
      siret = newUser.siret,
      bankId = newUser.bankId,
      role = newUser.role,
      authId = newUser.authId
    ))
  }

}
