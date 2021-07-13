package models


import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat, OWrites, Writes}


case class Bill(
                 id: Long,
                 customerId: Long,
//                 created: DateTime,
                 periodCovered: String,
                 billNumber: String,
               )

object Bill {
//  implicit val jodaWrites: Writes[DateTime] = JodaWrites.jodaDateWrites("yyyy-MM-dd")
  implicit val billFormat: OFormat[Bill] = Json.format[Bill]

}

case class BillWithData(
                 id: Long,
                 customer: Customer,
                 //                 created: DateTime,
                 periodCovered: String,
                 billNumber: String,
                 benefit: Seq[Benefit],
               ) {
}

object BillWithData {
  implicit val billWithData: OFormat[BillWithData] = Json.format[BillWithData]
  def fromBillAndCustomerTables(bill: Bill, customer: Customer, benefit: Seq[Benefit]): BillWithData = {
    BillWithData(
      id = bill.id,
      customer = customer,
      periodCovered = bill.periodCovered.toLowerCase.capitalize,
      billNumber = bill.billNumber,
      benefit = benefit,
    )
  }
}
