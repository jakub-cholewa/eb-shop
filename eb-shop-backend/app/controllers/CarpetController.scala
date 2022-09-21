package controllers

import com.mohiva.play.silhouette.api.actions.SecuredRequest

import javax.inject._
import models.{Carpet, CarpetRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class CarpetController @Inject()(carpetsRepo: CarpetRepository, cc: MessagesControllerComponents, scc: SilhouetteControllerComponents)(implicit ec: ExecutionContext) extends SilhouetteController(scc) {

  val carpetForm: Form[CreateCarpetForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(CreateCarpetForm.apply)(CreateCarpetForm.unapply)
  }

  val updateCarpetForm: Form[UpdateCarpetForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(UpdateCarpetForm.apply)(UpdateCarpetForm.unapply)
  }

  def getCarpets: Action[AnyContent] = SecuredAction.async { implicit request =>
    val carpets = carpetsRepo.list()
    carpets.map( carpet => Ok(Json.toJson(carpet)))
  }

  def deleteCarpet(id: Long): Action[AnyContent] = SecuredAction {
    carpetsRepo.delete(id)
    Redirect("/carpets")
  }

  def updateCarpet(id: Long): Action[AnyContent] = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    updateCarpetForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      carpet => {
        carpetsRepo.update(id, Carpet(carpet.id, carpet.name, carpet.price)).map { _ =>
          Redirect(routes.CarpetController.getCarpets()).flashing("success" -> "carpet updated")
        }
      }
    )
  }

  def addCarpet = SecuredAction.async { implicit request =>
    carpetForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      carpet => {
        carpetsRepo.create(carpet.name, carpet.price).map { _ =>
          Redirect(routes.CarpetController.getCarpets()).flashing("success" -> "carpet created")
        }
      }
    )
  }
}

case class CreateCarpetForm(name: String, price: BigDecimal)
case class UpdateCarpetForm(id: Long, name: String, price: BigDecimal)