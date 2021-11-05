package models

import play.api.libs.json.{Json, OFormat}

case class Bank(
               id: Long,
               name: String,
               bankCode: String,
               guichetCode: String,
               account: String,
               ribKey: BigDecimal,
               iban: String,
               userId: Long
               ){

}
object Bank {
  implicit val bank: OFormat[Bank] = Json.format[Bank]
}
