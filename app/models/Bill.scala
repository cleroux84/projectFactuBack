package models


import org.joda.time.DateTime
import play.api.libs.json.Json

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
  implicit val billFormat = Json.format[Bill]
}

