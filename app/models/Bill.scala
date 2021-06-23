package models


import org.joda.time.DateTime
import play.api.libs.json.{ Json, OWrites, Writes}


case class Bill(
                 id: Long,
                 customerId: Long,
//                 created: DateTime,
                 periodCovered: String,
                 billNumber: String,
                 benefit: String,
                 quantity: Int,
                 unitPrice: Int,
                 vatRate: Int
                   )

object Bill {
//  implicit val jodaWrites: Writes[DateTime] = JodaWrites.jodaDateWrites("yyyy-MM-dd")
  implicit val billFormat = Json.format[Bill]

}

