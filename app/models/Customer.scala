package models

import play.api.libs.json.{Format, Json}
import play.api.mvc.PathBindable
import slick.lifted.MappedTo


case class Customer(
                     id: Long,
                     civility: String,
                     firstName : String,
                     lastName: String,
                     email: String,
                     phone: String,
                     phone2: Option[String],
                     address: String,
                     city: String,
                     zipCode: String,
                     company: Option[String],
                     VATNumber: String,
                     bankId: Long
                   )
object Customer {
  implicit val customerFormat = Json.format[Customer]
}
