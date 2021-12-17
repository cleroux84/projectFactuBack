
import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import auth.AuthAction
import controllers.{BillController, CustomerController, UserController}
import models.User
import org.joda.time.DateTime
import org.mockito.Mockito
import org.mockito.Mockito.{mock, verify}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Play.materializer
import play.api.db.slick.DatabaseConfigProvider
import play.api.http.Status.CREATED
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{MessagesControllerComponents, Request, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, POST, await, contentAsJson, defaultAwaitTimeout, status}
import repositories.{BillRepository, CustomerRepository, UserRepository}
import services.BillService

import scala.concurrent.{ExecutionContext}


class UnitSpec extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar {

  lazy val appBuilder: GuiceApplicationBuilder = new GuiceApplicationBuilder()
  lazy val injector: Injector = appBuilder.injector()
  lazy val dbConfProvider: DatabaseConfigProvider = injector.instanceOf[DatabaseConfigProvider]
  implicit val mockEC: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  val billRepo = new BillRepository(dbConfProvider)(mockEC)

  "paymentStatus" should {
    "be latePayment" in {
      billRepo.composePaymentStatus(DateTime.parse("2020-10-10"), paid = false) mustBe("latePayment")
    }
    "be waitingPayment" in {
      val tenDaysFromToday = DateTime.now().plusDays(10)
      billRepo.composePaymentStatus(tenDaysFromToday, paid = false) mustBe("paid")
    }
    "be paid" in {
      billRepo.composePaymentStatus(DateTime.now(), paid = true) mustBe("paid")
    }
  }

//  "BillController GET" should {
//    "render the customerList" in {
//      val repo = mock[CustomerRepository]
//      val mockCC: MessagesControllerComponents = app.injector.instanceOf[MessagesControllerComponents]
//      val mockAuth: AuthAction = mock[AuthAction]
//      val controller = new CustomerController(mockCC, repo, mockAuth)
//      val list = controller.getCustomers().apply(FakeRequest(GET, "/listCustomer"))
//      status(list) mustBe Ok
//    }
//  }

}
