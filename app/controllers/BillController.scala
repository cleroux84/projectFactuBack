package controllers

import akka.http.scaladsl.model.DateTime
import models.{Bill, BillRepository}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class BillController @Inject()(cc: MessagesControllerComponents,
                               repo: BillRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def getBills: Action[AnyContent] = Action.async { implicit request =>
    repo.getListBill.map({ billWithCustomerData =>
      Ok(Json.toJson(billWithCustomerData))
    })
  }

  def deleteBill(id: Long): Action[AnyContent] = Action.async { implicit request =>
    repo.deleteBill(id).map(_ => Redirect(routes.BillController.getBills()))
  }

  case class CreateBillForm(
                           //TODO : ce que je veux recevoir du front
//                               println(DateTime.now())
                           customerId: Long,
//                           created: DateTime
                           periodCovered: String,
//                           billNumber: String,
                           benefit: String,
                           quantity: Int,
                           unitPrice: Int,
                           vatRate: Int
                           ){
    def toBillCustom: Bill = Bill(
      id = 0L,
      customerId = this.customerId,
      periodCovered = this.periodCovered,
//      billNumber = this.billNumber,
      billNumber = repo.composeBillNumber(),
      benefit = this.benefit,
      quantity = this.quantity,
      unitPrice = this.unitPrice,
      vatRate = this.vatRate
    )
  }

  object CreateBillForm {
    implicit val reader = Json.reads[CreateBillForm]
  }

  def addBill: Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateBillForm] match {
      case JsSuccess(createCustomerForm, _) =>
        repo.addBill(createCustomerForm.toBillCustom)
      case JsError(errors) => println(errors)
    }
    Future.successful(Ok)
  }

}
