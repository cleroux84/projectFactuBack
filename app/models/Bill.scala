package models

import org.joda.time.DateTime
import play.api.libs.json.{JodaReads, JodaWrites, Json, OFormat, Reads, Writes}



case class Bill(
                 id: Long,
                 customerId: Long,
                 created: DateTime,
                 periodCovered: String,
                 billNumber: String,
                 userId: Long,
                 paid: Boolean,
                 paymentDate: Option[DateTime]
               )

object Bill {
  implicit val jodaWrites: Writes[DateTime] = JodaWrites.jodaDateWrites("dd-MM-yyyy")
  implicit val jodaReads: Reads[DateTime] = JodaReads.JodaDateReads
  implicit val billFormat: OFormat[Bill] = Json.format[Bill]
//  implicit val billWrites = Json.reads[Bill]
//  implicit val billReads = Json.writes[Bill]

}

//voir https://gist.github.com/kencoba/1875983 pour créer un pattern decorator
case class BillWithData(
                 id: Long,
                 customer: Customer,
                 created: DateTime,
                 periodCovered: String,
                 billNumber: String,
                 benefit: Seq[BenefitWithMount],
                 amountHt: BigDecimal,
                 amountTtc: BigDecimal,
                 invoiceDueBy: DateTime,
                 paid: Boolean,
                 paymentDate: Option[DateTime],
                 paymentStatus: String,
                 userId: Long
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
                                paymentStatus: String,
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
      invoiceDueBy = bill.created.plusDays(15),
      paid= bill.paid,
      paymentDate = bill.paymentDate,
      paymentStatus = paymentStatus,
      userId = bill.userId
    )
  }
}
