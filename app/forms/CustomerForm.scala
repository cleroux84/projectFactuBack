package forms

import play.api.libs.json.Json

object CustomerForm {
  import play.api.data.Forms._
  import play.api.data.Form

  case class DataCustomer(
                          civility: String,
                          firstName: String,
                          lastName: String,
                          email: String,
                          phone: String,
                          phone2: Option[String],
                          address: String,
                          city: String,
                          zipCode: String,
                          company: Option[String],
                          VATNumber: String
                         )

  object DataCustomer {
  implicit val reader = Json.reads[DataCustomer]
}

}
