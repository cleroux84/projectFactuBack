package forms

import models.Benefit
import play.api.libs.json.{Json, Reads}

object BenefitForm {
  case class CreateBenefitForm(
                                name: String,
                                quantity: BigDecimal,
                                unitPrice: BigDecimal,
                                vatRate: BigDecimal
                              ){
    def toBenefitCustom(billId: Long): Benefit = Benefit(
      id = 0L,
      billId = billId,
      name = this.name,
      quantity = this.quantity,
      unitPrice = this.unitPrice,
      vatRate = this.vatRate
    )
  }

  object CreateBenefitForm {
    implicit val reader: Reads[CreateBenefitForm] = Json.reads[CreateBenefitForm]
  }

}
