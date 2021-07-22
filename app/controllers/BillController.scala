package controllers

import akka.http.scaladsl.model.DateTime
import com.hhandoko.play.pdf.PdfGenerator
import models.{BenefitRepository, Bill, BillRepository}
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


/**
 * A controller for bill.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class BillController @Inject()(
                               dbConfigProvider: DatabaseConfigProvider,
                               cc: MessagesControllerComponents,
                               repo: BillRepository,
                               repoBenefit: BenefitRepository,
                               env: Environment,
                              )
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

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

  def exportBillPdf(id: Long): Action[AnyContent] = Action.async { implicit r =>
    repo.findBill(id).map { billSeq =>
      pdfGen.ok(views.html.originalBill(billSeq.head, "etreprise@mail.com"), "http://localhost:9000") }
    }

  def getBills: Action[AnyContent] = Action.async { implicit request =>
    repo.getListBill.map({ billWithCustomerData =>
      Ok(Json.toJson(billWithCustomerData))
    })
  }

  //fonctionne mais inutile pour les factures :
  //  def deleteBill(id: Long): Action[AnyContent] = Action.async { implicit request =>
  //    repo.deleteBill(id).map(_ => Redirect(routes.BillController.getBills()))
  //  }

  def addBill: Action[JsValue] = Action.async(parse.json) { implicit request =>
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
        println(errors)
        Future.successful(BadRequest)
      }
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