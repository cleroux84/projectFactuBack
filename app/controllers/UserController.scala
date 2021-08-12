package controllers

import auth.AuthAction
import forms.UserForm._
import models. UserRepository
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, MessagesControllerComponents}
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(
                              cc: MessagesControllerComponents,
                              repo: UserRepository,
                              authAction: AuthAction
                              )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getUserList: Action[AnyContent] = Action.async { implicit request =>
    repo.getUserList.map({ users =>
      Ok(Json.toJson(users))
    })
  }

  def getCurrentUser(id: Long): Action[AnyContent] = Action.async { implicit req =>
    repo.getUser(id).map({ user =>
      Ok(Json.toJson(user))})
  }

  def testUser(id: Long): Action[AnyContent] = Action.async { implicit request =>
    repo.getUser(id).map { x =>
      Ok(Json.toJson(x))
    }
  }

  def addUser: Action[JsValue] = Action.async(parse.json) {implicit r =>
    r.body.validate[CreateUserForm] match {
      case JsSuccess(createUserForm, _) =>
        repo.addUser(createUserForm.toUserCustom)
      case JsError(errors) => println(errors)
    }
    Future.successful(Ok)
  }

}
