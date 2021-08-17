package controllers

import auth.AuthAction
import forms.BankForm.CreateBankForm
import models.BankRepository
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, MessagesControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BankController @Inject()(
                              cc: MessagesControllerComponents,
                              repo: BankRepository,
                              authAction: AuthAction
                              )(implicit ex : ExecutionContext) extends AbstractController(cc) {

  def addBank: Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateBankForm] match {
      case  JsSuccess(createBankForm, _) =>
        repo.addBank(createBankForm.tobankCustom)
      case JsError(errors) => println(errors)
    }
    Future.successful(Ok)
  }

  def updateBank(id: Long): Action[JsValue] = Action.async(parse.json) { implicit r =>
    r.body.validate[CreateBankForm] match {
      case JsSuccess(data, _) =>
        repo.updateBank(id, data.name, data.bankCode, data.guichetCode, data.account, data.ribKey, data.iban).map{ _ =>Ok}
      case JsError(errors) => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" ->JsError.toJson(errors))))
    }
  }

  def deleteBank(id: Long): Action[AnyContent] = Action.async { implicit r =>
    repo.deleteBank(id).map(_ => Redirect(routes.UserController.getUserList()))
  }

}
