import org.joda.time.DateTime
import slick.lifted.TableQuery
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.text.DecimalFormat



val date = DateTime.now().toString()
val year = date.split("-")(0)
val lastBillNumber = "006".toInt
val lastBillInt = lastBillNumber + 1
val nf = new DecimalFormat("000")
val result = year + "-" + nf.format(lastBillInt)

val testBillN: Option[String] = Some("2005")
val lastBillYear = testBillN.map(_.split("-")(1))



//for {
//  billId <- billDAO.save(bill)
//  _ <- benefitDAO.save(benefitsForms.map(_.toBenefit(billId)))
//
//} yield ()