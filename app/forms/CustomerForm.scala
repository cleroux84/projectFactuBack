package forms

import play.api.libs.json.Json
import models.Customer


object CustomerForm {

  case class CreateCustomerForm(
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
                         ){
    def toCustomerCustom: Customer = Customer(
      id = 0L,
      civility = this.civility,
      firstName = this.firstName.toUpperCase(),
      lastName = this.lastName.toUpperCase(),
      email = this.email,
      phone = this.phone,
      phone2 = this.phone2,
      address = this.address,
      city = this.city,
      zipCode = this.zipCode,
      company = this.company,
      VATNumber = this.VATNumber
    )
  }

  object CreateCustomerForm {
  implicit val reader = Json.reads[CreateCustomerForm]
}

}
