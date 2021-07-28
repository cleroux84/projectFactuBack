package models

import org.joda.time.DateTime
import play.api.libs.json.{JodaReads, JodaWrites, Json, OFormat, Reads, Writes}



case class Bill(
                 id: Long,
                 customerId: Long,
                 created: DateTime,
                 periodCovered: String,
                 billNumber: String,
               )

object Bill {
  implicit val jodaWrites: Writes[DateTime] = JodaWrites.jodaDateWrites("dd-MM-yyyy")
  implicit val jodaReads: Reads[DateTime] = JodaReads.JodaDateReads
  implicit val billFormat: OFormat[Bill] = Json.format[Bill]
//  implicit val billWrites = Json.reads[Bill]
//  implicit val billReads = Json.writes[Bill]

}

case class BillWithData(
                 id: Long,
                 customer: Customer,
                 created: DateTime,
                 periodCovered: String,
                 billNumber: String,
                 benefit: Seq[BenefitWithMount],
                 amountHt: BigDecimal,
                 amountTtc: BigDecimal,
               ) {
}

object BillWithData {
  implicit val jodaWrites: Writes[DateTime] = JodaWrites.jodaDateWrites("dd-MM-yyyy")
  implicit val jodaReads: Reads[DateTime] = JodaReads.JodaDateReads
  implicit val billWithData: OFormat[BillWithData] = Json.format[BillWithData]
  def fromBillAndCustomerTables(bill: Bill,
                                customer: Customer,
                                benefit: Seq[BenefitWithMount],
                                amountHt: BigDecimal,
                                amountTtc: BigDecimal,
                               ): BillWithData = {
    BillWithData(
      id = bill.id,
      customer = customer,
      periodCovered = bill.periodCovered.toLowerCase.capitalize,
      billNumber = bill.billNumber,
      created = bill.created,
      benefit = benefit,
      amountHt = amountHt,
      amountTtc = amountTtc,
    )
  }
}