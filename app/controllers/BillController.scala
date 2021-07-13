package controllers

import akka.http.scaladsl.model.DateTime
import models.{Benefit, BenefitRepository, Bill, BillRepository}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps

class BillController @Inject()(cc: MessagesControllerComponents,
                               repo: BillRepository, repoBenefit: BenefitRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def getBills: Action[AnyContent] = Action.async { implicit request =>
    repo.getListBill.map({ billWithCustomerData =>
      Ok(Json.toJson(billWithCustomerData))
    })
  }

  case class CreateBillForm(
                           customerId: Long,
//                           created: DateTime
                           periodCovered: String,
                           ){
    def toBillCustom(yearNumber: String): Bill = Bill(
      id = 0L,
      customerId = this.customerId,
      periodCovered = this.periodCovered,
      billNumber = yearNumber,
    )
  }

  object CreateBillForm {
    implicit val reader = Json.reads[CreateBillForm]
  }


  //fonctionnait mais inutile pour les factures :
  //  def deleteBill(id: Long): Action[AnyContent] = Action.async { implicit request =>
  //    repo.deleteBill(id).map(_ => Redirect(routes.BillController.getBills()))
  //  }

//  case class CreateBenefitForm(
////                              billId: Long,
//                              name: String,
//                              quantity: BigDecimal,
//                              unitPrice: BigDecimal,
//                              vatRate: BigDecimal
//                              ){
//    def toBenefitCustom(billId: Long): Benefit = Benefit(
//      id = 0L,
//      billId = billId,
//      name = this.name,
//      quantity = this.quantity,
//      unitPrice = this.unitPrice,
//      vatRate = this.vatRate
//    )
//  }
//
//  object CreateBenefitForm {
//    implicit val reader = Json.reads[CreateBenefitForm]
//  }

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
      case JsSuccess(createBillForm, _) =>
        repo.composeBillNumber().flatMap { number =>
          repo.addBill(createBillForm.toBillCustom(number))
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