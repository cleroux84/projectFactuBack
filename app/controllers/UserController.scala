package controllers

import auth.AuthAction
import forms.UserForm._
import models.User
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, MessagesControllerComponents}
import repositories.{BankRepository, UserRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(
                              cc: MessagesControllerComponents,
                              repo: UserRepository,
                              authAction: AuthAction
                              )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getUserList: Action[AnyContent] = authAction.async { implicit request =>
    repo.getUserList.map({ users =>
      Ok(Json.toJson(users))
    })
  }

  def getCurrentUser(email: String): Action[AnyContent] = authAction.async { implicit req =>
//  def getCurrentUser(email: String): Action[AnyContent] = Action.async { implicit req =>
    repo.getUser(email).map({ user =>
      Ok(Json.toJson(user))})
  }

  def getUserWithBank(email: String): Action[AnyContent] = authAction.async { implicit r =>
    repo.getUserWithBank(email).map { userWithBank =>
      Ok(Json.toJson(userWithBank))
    }
  }

  def addUser: Action[JsValue] = authAction.async(parse.json) {implicit r =>
    r.body.validate[CreateUserForm] match {
      case JsSuccess(createUserForm, _) =>
        repo.addUser(createUserForm.toUserCustom)
      case JsError(errors) => println(errors)
    }
    Future.successful(Ok)
  }

  def updateUser(id: Long): Action[JsValue] = authAction.async(parse.json) { implicit r =>
    r.body.validate[CreateUserForm] match {
      case JsSuccess(data, _) =>
        repo.updateUser(id, data.civility, data.firstName, data.lastName.toUpperCase(), data.email, data.phone, data.address, data.city, data.zipCode, data.siret, data.role, data.authId).map{ _ =>Ok}
      case JsError(errors) => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" ->JsError.toJson(errors))))
    }
  }

  def deleteUser(id: Long): Action[AnyContent] = Action.async { implicit request =>
    repo.deleteUser(id).map(_ => Redirect(routes.UserController.getUserList()))
  }

}
