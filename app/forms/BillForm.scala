package forms

import forms.BenefitForm.CreateBenefitForm
import models.Bill
import org.joda.time.DateTime
import play.api.libs.json.{JodaReads, JodaWrites, Json, Reads, Writes}

object BillForm {
  case class CreateBillForm(
                             customerId: Long,
                             created: DateTime,
                             periodCovered: String,
                             benefits: Seq[CreateBenefitForm],
                             userId: Long,
                             paid: Boolean,
                             paymentDate: Option[DateTime]
                           ){
    def toBillCustom(yearNumber: String): Bill = Bill(
      id = 0L,
      customerId = this.customerId,
      periodCovered = this.periodCovered,
      billNumber = yearNumber,
      created = this.created,
      userId = this.userId,
      paid = this.paid,
      paymentDate = this.paymentDate
    )

  }

  object CreateBillForm {
    implicit val jodaWrites: Writes[DateTime] = JodaWrites.jodaDateWrites("yyyy-MM-dd")
    implicit val jodaReads: Reads[DateTime] = JodaReads.JodaDateReads
    implicit val reader: Reads[CreateBillForm] = Json.reads[CreateBillForm]
  }

}
