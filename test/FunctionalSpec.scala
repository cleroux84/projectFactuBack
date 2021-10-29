import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers._

class FunctionalSpec extends PlaySpec with GuiceOneAppPerSuite {
  "Routes" should {
    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/boom")).map(status(_))mustBe Some(NOT_FOUND)
    }

    //avant auth
    "send 200 on a good request" in  {
      route(app, FakeRequest(GET, "/listBill")).map(status(_)) mustBe Some(OK)
    }
  }
}
