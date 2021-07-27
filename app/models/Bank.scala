package models

import play.api.libs.json.{Json, OFormat}

case class Bank(
               id: Long,
               customerId: Long,
               name: String,
               bankCode: String,
               guichetCode: String,
               account: String,
               ribKey: BigDecimal,
               iban: String
               ){

}
object Bank {
  implicit val bank: OFormat[Bank] = Json.format[Bank]
}
