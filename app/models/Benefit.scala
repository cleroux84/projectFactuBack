package models

import play.api.libs.json.{Json, OFormat}

case class Benefit(
                  id: Long,
                  billId: Long,
                  periodCovered: String,
                  name: String,
                  quantity: BigDecimal,
                  unitPrice: BigDecimal,
                  vatRate: BigDecimal
                  )

object Benefit {
  implicit val benefitFormat: OFormat[Benefit] = Json.format[Benefit]
}

//case class BenefitWithBill(
//                          id: Long,
//                          bill: Bill,
//                          periodCovered: String,
//                          name: String,
//                          quantity: BigDecimal,
//                          unitPrice: BigDecimal,
//                          vatRate: BigDecimal
//                          )
//
//object BenefitWithBill {
//  implicit val benefitWithBill: OFormat[BenefitWithBill] = Json.format[BenefitWithBill]
//  def fromBenefitAndBillTables(benefit: Benefit, bill: Bill): BenefitWithBill = {
//    BenefitWithBill(
//      id = benefit.id,
//      bill = bill,
//      periodCovered = benefit.periodCovered,
//      name = benefit.name,
//      quantity = benefit.quantity,
//      unitPrice = benefit.unitPrice,
//      vatRate = benefit.vatRate
//    )
//  }
//}