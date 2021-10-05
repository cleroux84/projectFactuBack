package controllers

import akka.http.scaladsl.model.DateTime
import auth.AuthAction
import com.hhandoko.play.pdf.PdfGenerator
import models.{BankRepository, Benefit, BenefitRepository, Bill, BillRepository, BillWithData, User, UserRepository, UserWithBank}
import forms.BenefitForm._
import forms.BillForm._
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import play.api.{Configuration, Environment}

import javax.inject.{Inject, Singleton}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps


@Singleton
class BillController @Inject()(
                               cc: MessagesControllerComponents,
                               repo: BillRepository,
                               repoBenefit: BenefitRepository,
                               repoUser: UserRepository,
                               repoBank: BankRepository,
                               env: Environment,
                               authAction: AuthAction
                              )
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {

  val pdfGen = new PdfGenerator(env)
  pdfGen.loadLocalFonts(Seq(
    "fonts/Roboto-Black.ttf",
    "fonts/Roboto-BlackItalic.ttf",
    "fonts/Roboto-Bold.ttf",
    "fonts/Roboto-BoldItalic.ttf",
    "fonts/Roboto-Italic.ttf",
    "fonts/Roboto-Light.ttf",
    "fonts/Roboto-LightItalic.ttf",
    "fonts/Roboto-Medium.ttf",
    "fonts/Roboto-MediumItalic.ttf",
    "fonts/Roboto-Regular.ttf",
    "fonts/Roboto-Thin.ttf",
    "fonts/Roboto-ThinItalic.ttf",
  ))

  def exportBillPdf(id: Long, userMail: String): Action[AnyContent] = Action.async { implicit r =>
    repo.findBill(id).flatMap { billSeq: Seq[BillWithData] =>
      repoUser.getUser(userMail).flatMap { user =>
        repoBank.getBank(user).map { bank =>
          val userAndBank = UserWithBank.fromUserToBank(user, bank)
          pdfGen.ok(views.html.originalBill(billSeq.head, userAndBank), "http://localhost:9000")
        }
      }
    }
  }
  def getThisBill(id: Long): Action[AnyContent] = Action.async { implicit r =>
    repo.findBill(id).map { billSeq =>
      Ok(Json.toJson(billSeq))
    }
  }

  def getBillsByUser(userId: Long): Action[AnyContent] = authAction.async { implicit request =>
    repo.getListBillByUser(userId).map({ billWithCustomerData =>
      Ok(Json.toJson(billWithCustomerData))
    })
  }

  def getBills: Action[AnyContent] = authAction.async { implicit request =>
    repo.getListBill.map({ billWithCustomerData =>
      Ok(Json.toJson(billWithCustomerData))
    })
  }

  def getLateBills: Action[AnyContent] = authAction.async { implicit request =>
    repo.getLateBills.map({ billWithCustomerData =>
      Ok(Json.toJson(billWithCustomerData))
    })
  }

  def getUnpaidBills: Action[AnyContent] = authAction.async { implicit request =>
    repo.getUnpaidBills.map({ billSeq =>
      Ok(Json.toJson(billSeq))
    })
  }

  def getUnpaidBillsByUser(userId: Long): Action[AnyContent] = authAction.async { implicit request =>
    repo.getUnpaidBillsByUser(userId).map({ billSeq =>
      Ok(Json.toJson(billSeq))
    })
  }

  def getLateBillsByUser(userId: Long): Action[AnyContent] = authAction.async { implicit request =>
    repo.getLateBillByUser(userId).map({ billLate =>
      Ok(Json.toJson(billLate))
    })
  }

  def addBill: Action[JsValue] = authAction.async(parse.json) { implicit request =>
    request.body.validate[CreateBillForm] match {
      case JsSuccess(createBillForm, _) =>
        repo.composeBillNumber().flatMap { number =>
          repo.addBill(createBillForm.toBillCustom(number)).flatMap { billId =>
            repoBenefit.addBenefit(createBillForm.benefits.map(_.toBenefitCustom(billId)))
          }
        }
        Future.successful(Ok)
//        for {
//          number <- repo.composeBillNumber()
//          billId <- repo.addBill(createBillForm.toBillCustom(number))
//          _ <- repoBenefit.addBenefit(createBillForm.benefits.map(_.toBenefitCustom(billId)))
//        } yield ()
      case JsError(errors) =>
        println("error billController")
        println(errors)
        Future.successful(BadRequest)
      }
    }

  def updatePayment(id: Long): Action[JsValue] = authAction.async(parse.json) { implicit request =>
    request.body.validate[CreateBillForm] match {
      case JsSuccess(data, _) =>
        repo.updatePayment(id, data.paid, data.paymentDate).map(_ => Ok)
      case JsError(errors) => Future.successful(BadRequest(Json.obj()))
    }
  }

  //fonctionne mais inutile pour les factures :
  //  def deleteBill(id: Long): Action[AnyContent] = Action.async { implicit request =>
  //    repo.deleteBill(id).map(_ => Redirect(routes.BillController.getBills()))
  //  }

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