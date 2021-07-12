package models

import play.api.libs.json.{Json, OFormat}

case class Benefit(
                  id: Long,
                  billId: Long,
                  periodCovered: String,
                  name: String,
                  quantity: BigDecimal,
                  unitPrice: BigDecimal,
                  vatRate: BigDecimal,
                  )

object Benefit {
  implicit val benefitFormat: OFormat[Benefit] = Json.format[Benefit]
}

//case class BenefitData(
//                          id: Long,
//                          periodCovered: String,
//                          name: String,
//                          quantity: BigDecimal,
//                          unitPrice: BigDecimal,
//                          vatRate: BigDecimal,
//                          totalHT: BigDecimal
//                          )
//
//object BenefitData {
//  implicit val benefitData: OFormat[BenefitData] = Json.format[BenefitData]
//  def fromTables(benefit: BenefitData): BenefitData = {
//    BenefitData(
//      id = benefit.id,
//      periodCovered = benefit.periodCovered,
//      name = benefit.name,
//      quantity = benefit.quantity,
//      unitPrice = benefit.unitPrice,
//      vatRate = benefit.vatRate,
//      totalHT = benefit.unitPrice * benefit.quantity
//    )
//  }
//}