package controllers

import models.UserRepository
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, MessagesControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(
                              cc: MessagesControllerComponents,
                              repo: UserRepository
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

}
