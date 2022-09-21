package controllers

import com.mohiva.play.silhouette.api.actions.SecuredRequest

import javax.inject._
import models.{Desk, DeskRepository}
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
class DeskController @Inject()(desksRepo: DeskRepository, cc: MessagesControllerComponents, scc: SilhouetteControllerComponents)(implicit ec: ExecutionContext) extends SilhouetteController(scc) {

  val deskForm: Form[CreateDeskForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(CreateDeskForm.apply)(CreateDeskForm.unapply)
  }

  val updateDeskForm: Form[UpdateDeskForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(UpdateDeskForm.apply)(UpdateDeskForm.unapply)
  }

  def getDesks: Action[AnyContent] = SecuredAction.async { implicit request =>
    val desks = desksRepo.list()
    desks.map( desk => Ok(Json.toJson(desk)))
  }

  def deleteDesk(id: Long): Action[AnyContent] = SecuredAction {
    desksRepo.delete(id)
    Redirect("/desks")
  }

  def updateDesk(id: Long): Action[AnyContent] = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    updateDeskForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      desk => {
        desksRepo.update(id, Desk(desk.id, desk.name, desk.price)).map { _ =>
          Redirect(routes.DeskController.getDesks()).flashing("success" -> "desk updated")
        }
      }
    )
  }

  def addDesk = SecuredAction.async { implicit request =>
    deskForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      desk => {
        desksRepo.create(desk.name, desk.price).map { _ =>
          Redirect(routes.DeskController.getDesks()).flashing("success" -> "desk created")
        }
      }
    )
  }
}

case class CreateDeskForm(name: String, price: BigDecimal)
case class UpdateDeskForm(id: Long, name: String, price: BigDecimal)