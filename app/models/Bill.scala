package models


import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat, OWrites, Writes}


case class Bill(
                 id: Long,
                 customerId: Long,
//                 created: DateTime,
                 periodCovered: String,
                 billNumber: String,
                 benefit: String,
                 quantity: BigDecimal,
                 unitPrice: BigDecimal,
                 vatRate: BigDecimal
                   )

object Bill {
//  implicit val jodaWrites: Writes[DateTime] = JodaWrites.jodaDateWrites("yyyy-MM-dd")
  implicit val billFormat: OFormat[Bill] = Json.format[Bill]

}

case class BillWithData(
                 id: Long,
                 customer: Customer,
                 //                 created: DateTime,
//                 periodCovered: String,
                 billNumber: String,
                 benefit: Seq[Benefit],
//                 quantity: BigDecimal,
//                 unitPrice: BigDecimal,
//                 vatRate: BigDecimal,
//                 totalHT: BigDecimal,
//                 totalTTC: BigDecimal
               ) {
}
//TODO drop colomn in evolution

//Fait avant creation table benefit :
//object BillWithCustomerData {
//  implicit val billWithCustomerData: OFormat[BillWithCustomerData] = Json.format[BillWithCustomerData]
//  def fromBillAndCustomerTables(bill: Bill, customer: Customer): BillWithCustomerData = {
//    BillWithCustomerData(
//      id = bill.id,
//      customer = customer,
//      periodCovered = bill.periodCovered.toLowerCase.capitalize,
//      billNumber = bill.billNumber,
//      benefit = bill.benefit,
//      quantity = bill.quantity,
//      unitPrice = bill.unitPrice,
//      vatRate = bill.vatRate,
//      totalHT = bill.unitPrice * bill.quantity,
//      totalTTC = (bill.unitPrice * bill.quantity) * (bill.vatRate/100)
//    )
//  }
//}

object BillWithData {
  implicit val billWithData: OFormat[BillWithData] = Json.format[BillWithData]
  def fromBillAndCustomerTables(bill: Bill, customer: Customer, benefit: Seq[Benefit]): BillWithData = {
    BillWithData(
      id = bill.id,
      customer = customer,
//      periodCovered = benefit.periodCovered.toLowerCase.capitalize,
      billNumber = bill.billNumber,
      benefit = benefit,
//      quantity = benefit.quantity,
//      unitPrice = benefit.unitPrice,
//      vatRate = benefit.vatRate,
//      totalHT = benefit.unitPrice * benefit.quantity,
//      totalTTC = (benefit.unitPrice * benefit.quantity) * (benefit.vatRate/100)
    )
  }
}
