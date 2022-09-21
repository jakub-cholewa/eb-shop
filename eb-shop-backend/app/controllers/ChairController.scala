package controllers

import com.mohiva.play.silhouette.api.actions.SecuredRequest

import javax.inject._
import models.{Chair, ChairRepository}
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
class ChairController @Inject()(chairsRepo: ChairRepository, cc: MessagesControllerComponents, scc: SilhouetteControllerComponents)(implicit ec: ExecutionContext) extends SilhouetteController(scc) {

  val chairForm: Form[CreateChairForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(CreateChairForm.apply)(CreateChairForm.unapply)
  }

  val updateChairForm: Form[UpdateChairForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(UpdateChairForm.apply)(UpdateChairForm.unapply)
  }

  def getChairs: Action[AnyContent] = SecuredAction.async { implicit request =>
    val chairs = chairsRepo.list()
    chairs.map( chair => Ok(Json.toJson(chair)))
  }

  def deleteChair(id: Long): Action[AnyContent] = SecuredAction {
    chairsRepo.delete(id)
    Redirect("/chairs")
  }

  def updateChair(id: Long): Action[AnyContent] = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    updateChairForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      chair => {
        chairsRepo.update(id, Chair(chair.id, chair.name, chair.price)).map { _ =>
          Redirect(routes.ChairController.getChairs()).flashing("success" -> "chair updated")
        }
      }
    )
  }

  def addChair = SecuredAction.async { implicit request =>
    chairForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      chair => {
        chairsRepo.create(chair.name, chair.price).map { _ =>
          Redirect(routes.ChairController.getChairs()).flashing("success" -> "chair created")
        }
      }
    )
  }
}

case class CreateChairForm(name: String, price: BigDecimal)
case class UpdateChairForm(id: Long, name: String, price: BigDecimal)
