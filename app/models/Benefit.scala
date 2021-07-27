package models

import play.api.libs.json.{Json, OFormat}

case class Benefit(
                  id: Long,
                  billId: Long,
                  name: String,
                  quantity: BigDecimal,
                  unitPrice: BigDecimal,
                  vatRate: BigDecimal,
                  )

object Benefit {
  implicit val benefitFormat: OFormat[Benefit] = Json.format[Benefit]
}

case class BenefitWithMount(id: Long,
                            billId: Long,
                            name: String,
                            quantity: BigDecimal,
                            unitPrice: BigDecimal,
                            vatRate: BigDecimal,
                            amountHt: BigDecimal,
                            amountTtc: BigDecimal
                           ){
}

object BenefitWithMount {
  implicit val benefitWithMount: OFormat[BenefitWithMount] = Json.format[BenefitWithMount]
  def fromBenefitToAmounts(benefit: Benefit): BenefitWithMount = {
    BenefitWithMount(
            id = benefit.id,
            billId = benefit.billId,
            name = benefit.name,
            quantity = benefit.quantity,
            unitPrice = benefit.unitPrice,
            vatRate = benefit.vatRate,
            amountHt = (benefit.quantity * benefit.unitPrice).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble,
            amountTtc = (benefit.quantity * benefit.unitPrice) * (1 + (benefit.vatRate/100)).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    )
  }
}
