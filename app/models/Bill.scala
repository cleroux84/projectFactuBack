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

case class BillWithCustomerData(
                 id: Long,
                 customer: Customer,
                 //                 created: DateTime,
                 periodCovered: String,
                 billNumber: String,
                 benefit: String,
                 quantity: BigDecimal,
                 unitPrice: BigDecimal,
                 vatRate: BigDecimal,
                 totalHT: BigDecimal,
                 totalTTC: BigDecimal
               ) {
}

object BillWithCustomerData {
  implicit val billWithCustomerData: OFormat[BillWithCustomerData] = Json.format[BillWithCustomerData]
  def fromBillAndCustomerTables(bill: Bill, customer: Customer): BillWithCustomerData = {
    BillWithCustomerData(
      id = bill.id,
      customer = customer,
      periodCovered = bill.periodCovered.toLowerCase.capitalize,
      billNumber = bill.billNumber,
      benefit = bill.benefit,
      quantity = bill.quantity,
      unitPrice = bill.unitPrice,
      vatRate = bill.vatRate, //TODO decimal
      totalHT = bill.unitPrice * bill.quantity, //TODO decimal
      totalTTC = (bill.unitPrice * bill.quantity) * (bill.vatRate/100) //TODO decimal
    )
  }
}
