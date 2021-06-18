package controllers

import models.CustomerRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.mvc.BaseController
import javax.inject._
import scala.concurrent.ExecutionContext
import play.api.libs.json.Json

class CustomerController @Inject()(cc: MessagesControllerComponents, repo: CustomerRepository)(implicit ec: ExecutionContext) extends AbstractController(cc){
    val customerForm: Form[CreateCustomerForm] = Form {
      mapping(
            "civility" -> nonEmptyText,
            "firstName" -> nonEmptyText,
            "lastName" -> nonEmptyText,
            "email" -> email,
            "phone" -> nonEmptyText,
            "phone2" -> optional(text),
            "address" -> nonEmptyText,
            "city" -> nonEmptyText,
            "zipCode" -> nonEmptyText,
            "company" -> optional(text)
          )(CreateCustomerForm.apply)(CreateCustomerForm.unapply)
    }

    def index() = Action { implicit request: Request[AnyContent] =>
      Ok(views.html.index("tata"))
    }

    def getCustomers = Action.async { implicit request =>
      repo.getList().map( { customer =>
        println(customer)
        Ok(Json.toJson(customer))
      })
  }

  }
    case class CreateCustomerForm(
                                   civility: String,
                                   firstName: String,
                                   lastName: String,
                                   email: String,
                                   phone: String,
                                   phone2: Option[String],
                                   address: String,
                                   city: String,
                                   zipCode: String,
                                   company: Option[String]
                                 )
