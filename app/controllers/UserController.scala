package controllers

import auth.AuthAction
import forms.UserForm._
import models.{BankRepository, User, UserRepository}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, MessagesControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(
                              cc: MessagesControllerComponents,
                              repo: UserRepository,
                              bankRepo: BankRepository,
                              authAction: AuthAction
                              )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getUserList: Action[AnyContent] = Action.async { implicit request =>
    repo.getUserList.map({ users =>
      Ok(Json.toJson(users))
    })
  }

  def getCurrentUser(email: String): Action[AnyContent] = Action.async { implicit req =>
    repo.getUser(email).map({ user =>
      Ok(Json.toJson(user))})
  }

  def getUserWithBank(email: String): Action[AnyContent] = Action.async { implicit r =>
    repo.getUserWithBank(email).map { userWithBank =>
      Ok(Json.toJson(userWithBank))
    }
  }
//  def testUser(id: Long): Action[AnyContent] = Action.async { implicit request =>
//    repo.getUser(id).map { x =>
//      Ok(Json.toJson(x))
//    }
//  }

  def addUser: Action[JsValue] = Action.async(parse.json) {implicit r =>
    r.body.validate[CreateUserForm] match {
      case JsSuccess(createUserForm, _) =>
        repo.addUser(createUserForm.toUserCustom)
      case JsError(errors) => println(errors)
    }
    Future.successful(Ok)
  }

  def updateUser(id: Long): Action[JsValue] = Action.async(parse.json) { implicit r =>
    r.body.validate[CreateUserForm] match {
      case JsSuccess(data, _) =>
        repo.updateUser(id, data.civility, data.firstName, data.lastName, data.email, data.phone, data.address, data.city, data.zipCode, data.siret, data.bankId, data.role, data.authId).map{ _ =>Ok}
      case JsError(errors) => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" ->JsError.toJson(errors))))
    }
  }

}
