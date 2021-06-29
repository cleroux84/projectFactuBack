package models

import play.api.libs.json.{Format, Json}
import play.api.mvc.PathBindable
import slick.lifted.MappedTo


case class Customer(
                     id: Long,
                     civility: String,
                     firstName : String,
                     lastName: String,
                     email: String,
                     phone: String,
                     phone2: Option[String],
                     address: String,
                     city: String,
                     zipCode: String,
                     company: Option[String],
                     VATNumber: String
                   )
object Customer {
  implicit val customerFormat = Json.format[Customer]
}

//case class CustomerId(value: Long) extends AnyVal with MappedTo[Long]
//object CustomerId {
//  implicit val formatter: Format[CustomerId] = Json.format[CustomerId]
//  implicit def pathBinder(implicit longBinder: PathBindable[Long]): PathBindable[CustomerId] {
//  def bind(key: String, value: String): Either[String, CustomerId]
//  def unbind(key: String, value: CustomerId): String
//  } = new PathBindable[CustomerId] {
//    override def bind(key: String, value: String): Either[String, CustomerId] =
//      for {
//        id <- longBinder.bind(key, value).isRight
//      } yield {
//        CustomerId(id)
//      }
//    override def unbind(key: String, value: CustomerId): String = value.value.toString
//  }
//}