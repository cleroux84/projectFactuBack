//package controllers
//
//import auth.AuthAction
//import models.{BankRepository, BenefitRepository, BillRepository, UserRepository}
//import org.scalatestplus.mockito.MockitoSugar
//import org.scalatestplus.play.PlaySpec
//import org.scalatestplus.play.guice.GuiceOneAppPerTest
//import play.api.Environment
//import play.api.mvc.{ControllerComponents, MessagesControllerComponents, Result, Results}
//import play.api.test.FakeRequest
//import play.api.test.Helpers._
//
//import scala.concurrent.{ExecutionContext, Future}
//
//class BillControllerSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar {
//  val mockBill: BillRepository = mock[BillRepository]
//  val mockBenefit: BenefitRepository = mock[BenefitRepository]
//  val mockUser: UserRepository = mock[UserRepository]
//  val mockBank: BankRepository = mock[BankRepository]
//  val mockEnv: Environment = mock[Environment]
//  val mockAuth: AuthAction = mock[AuthAction]
//  val mockCC: MessagesControllerComponents = app.injector.instanceOf[MessagesControllerComponents]
//  implicit val mockEC: ExecutionContext = app.injector.instanceOf[ExecutionContext]
//
//  val controller = new BillController(mockCC, mockBill, mockBenefit, mockUser, mockBank, mockEnv, mockAuth)
//  val method: Future[Result] = controller.getBills()(FakeRequest())
//
//  assert(status(method) == 200)
//}
//
