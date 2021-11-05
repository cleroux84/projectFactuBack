package repositories

import models.{Bank, User}

import scala.concurrent.Future

trait BankRepositoryTrait  {
  def addBank(bank: Bank): Future[String]
  def getBank(user: User): Future[Option[Bank]]
  def updateBank(id: Long, name: String, bankCode: String, guichetCode: String, account: String, ribKey: BigDecimal, iban: String, userId: Long): Future[Int]
  def deleteBank(id: Long): Future[Int]
}
