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

  //fonctionnait mais inutile pour les factures :
//  def deleteBill(id: Long): Action[AnyContent] = Action.async { implicit request =>
//    repo.deleteBill(id).map(_ => Redirect(routes.BillController.getBills()))
//  }

  case class CreateBillForm(
                           customerId: Long,
//                           created: DateTime
                           periodCovered: String,
                           benefit: String,
                           quantity: BigDecimal,
                           unitPrice: BigDecimal,
                           vatRate: BigDecimal
                           ){
    def toBillCustom(yearNumber: String): Bill = Bill(
      id = 0L,
      customerId = this.customerId,
      periodCovered = this.periodCovered,
      billNumber = yearNumber,
      benefit = this.benefit,
      quantity = this.quantity,
      unitPrice = this.unitPrice,
      vatRate = this.vatRate
    )
  }

  object CreateBillForm {
    implicit val reader = Json.reads[CreateBillForm]
  }

//  def addBill: Action[JsValue] = Action.async(parse.json) { implicit request =>
//    request.body.validate[CreateBillForm] match {
//      case JsSuccess(createCustomerForm, _) =>
//        repo.addBill(createCustomerForm.toBillCustom)
//      case JsError(errors) => println(errors)
//    }
//    Future.successful(Ok)
//  }

  def addBill: Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateBillForm] match {
      case JsSuccess(createCustomerForm, _) =>
        repo.composeBillNumber().flatMap { number =>
          repo.addBill(createCustomerForm.toBillCustom(number))
        }
//        for {
//          number <- repo.composeBillNumber2()
//          _ <- repo.addBill(createCustomerForm.toBillCustom(number))
//        } yield ()
      case JsError(errors) => println(errors)
    }
    Future.successful(Ok)
  }
}
// Option[_] : map / flatMap
// Future[String] : map { string <- Accessible ici }
// Future[String].flatten <- NON
// Seq[_]
// Future[Option[_]] => map puis map
// Seq[Option[_]]] => flatMap
// Future[Future[_]]] => flatMap
// Option[Option[_]]] => flatMap
// Seq[Seq[_]]] => flatMap => Seq[_]