import akka.http.scaladsl.model.DateTime
import org.joda.time.LocalDateTime
//import org.joda.time.DateTime
//import slick.lifted.TableQuery
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//import java.text.DecimalFormat
//
//
//
//val date = DateTime.now().toString()
//val year = date.split("-")(0)
//val lastBillNumber = "006".toInt
//val lastBillInt = lastBillNumber + 1
//val nf = new DecimalFormat("000")
//val result = year + "-" + nf.format(lastBillInt)
//
//val testBillN: Option[String] = Some("2005")
//val lastBillYear = testBillN.map(_.split("-")(1))

//for {
//  billId <- billDAO.save(bill)
//  _ <- benefitDAO.save(benefitsForms.map(_.toBenefit(billId)))
//
//} yield ()
import org.joda.time.DateTime
//
import java.text.SimpleDateFormat
//
val created: DateTime  = DateTime.now()
//val createdtest: DateTime  = 2021-10-01T17:14:37.202+02:00
val plusDays: DateTime  = DateTime.now().plusDays(15)
val x = new SimpleDateFormat("dd-MM-yyyy").format(created.toDate)
val y = new SimpleDateFormat("dd-MM-yyyy").format(plusDays.toDate)
//LocalDateTime.parse(y).isAfter(x)



//val x = "0 6875 976 75"
//val number = x.filter(Character.isDigit)
//number.replaceAll("..(?!$)", "$0 ")
