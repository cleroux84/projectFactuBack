package models

import play.api.libs.json.{Json, OFormat}

case class User(
               id: Long,
               civility: String,
               firstName: String,
               lastName: String,
               email: String,
               phone: String,
               address: String,
               city: String,
               zipCode: String,
               siret: String,
               role: Int,
               authId: String
               ) {
}

object User {
  implicit val user: OFormat[User] = Json.format[User]
}

case class UserWithBank(
                       user: User,
                       bank: Option[Bank]
                       )
object UserWithBank {
  implicit val userWithBank: OFormat[UserWithBank] = Json.format[UserWithBank]
  def fromUserToBank(user: User, bank: Option[Bank]): UserWithBank = {
    UserWithBank(
      user = user,
      bank = bank
    )
  }
}