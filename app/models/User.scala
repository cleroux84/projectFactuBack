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
               bankId: Long
               ) {
}

object User {
  implicit val user: OFormat[User] = Json.format[User]
}
