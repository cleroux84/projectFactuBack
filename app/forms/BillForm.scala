package forms

import forms.BenefitForm.CreateBenefitForm
import models.Bill
import play.api.libs.json.Json

object BillForm {
  case class CreateBillForm(
                             customerId: Long,
//                           created: DateTime
                             periodCovered: String,
                             benefits: Seq[CreateBenefitForm]
                           ){
    def toBillCustom(yearNumber: String): Bill = Bill(
      id = 0L,
      customerId = this.customerId,
      periodCovered = this.periodCovered,
      billNumber = yearNumber,
    )

  }

  object CreateBillForm {
    implicit val reader = Json.reads[CreateBillForm]
  }

}
