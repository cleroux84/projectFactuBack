package controllers

import models.BillRepository
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class BillController @Inject()(cc: MessagesControllerComponents,
                               repo: BillRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def getBills: Action[AnyContent] = Action.async { implicit request =>
    repo.getListBill.map({ bills =>
      Ok(Json.toJson(bills))
    })
  }


}
