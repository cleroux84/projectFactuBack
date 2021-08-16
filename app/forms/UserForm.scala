package forms

import models.User
import play.api.libs.json.Json

object UserForm {

  case class CreateUserForm(
                           civility: String,
                           firstName: String,
                           lastName: String,
                           email: String,
                           phone: String,
                           address: String,
                           city: String,
                           zipCode: String,
                           siret: String,
                           bankId: Long,
                           role: Int,
                           authId: String
                           ){
    def toUserCustom: User = User(
      id = 0L,
      civility = this.civility,
      firstName = this.firstName.toUpperCase(),
      lastName = this.lastName.toUpperCase(),
      email = this.email,
      phone = this.phone,
      address = this.address,
      city = this.city,
      zipCode = this.zipCode,
      siret = this.siret,
      bankId = this.bankId,
      role = this.role,
      authId = this.authId
    )
  }

  object CreateUserForm {
    implicit val reader = Json.reads[CreateUserForm]
  }

}
