
package controllers

import models. CustomerRepository
import play.api.mvc._
import forms.CustomerForm._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}

class CustomerController @Inject()(
                                    cc:MessagesControllerComponents,
                                    repo: CustomerRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getCustomers: Action[AnyContent] = Action.async { implicit request =>
    repo.getList.map({ customers =>
      Ok(Json.toJson(customers))
    })
  }

  def deleteCustomer(id: Long): Action[AnyContent] = Action.async { implicit request =>
    repo.deleteCustomer(id).map(_ => Redirect(routes.CustomerController.getCustomers()))
  }

  def addCustomer: Action[JsValue] = Action.async(parse.json) { implicit request =>
//    println(request.body) // -> parse.json avec body qui un json
    request.body.validate[CreateCustomerForm] match { // valide que le json correspond à une case class que j'ai créé
      case JsSuccess(createCustomerForm, _) =>
        repo.addCustomer(createCustomerForm.toCustomerCustom)
      case JsError(errors) => println(errors)
    }
    Future.successful(Ok)
  }

  def updateCustomer(id: Long): Action[JsValue] = Action.async(parse.json) {implicit request =>
    request.body.validate[CreateCustomerForm] match {
      case JsSuccess(data, _) =>
        repo.updateCustomer(id, data.civility, data.firstName, data.lastName, data.email, data.phone, data.phone2, data.company, data.address, data.city, data.zipCode, data.VATNumber).map{_ =>Ok}
      case JsError(errors) => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" ->JsError.toJson(errors))))
    }
  }
}