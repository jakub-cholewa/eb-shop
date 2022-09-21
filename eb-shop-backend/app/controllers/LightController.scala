package controllers

import com.mohiva.play.silhouette.api.actions.SecuredRequest

import javax.inject._
import models.{Light, LightRepository}
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
class LightController @Inject()(lightsRepo: LightRepository, cc: MessagesControllerComponents, scc: SilhouetteControllerComponents)(implicit ec: ExecutionContext) extends SilhouetteController(scc) {

  val lightForm: Form[CreateLightForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(CreateLightForm.apply)(CreateLightForm.unapply)
  }

  val updateLightForm: Form[UpdateLightForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(UpdateLightForm.apply)(UpdateLightForm.unapply)
  }

  def getLights: Action[AnyContent] = SecuredAction.async { implicit request =>
    val lights = lightsRepo.list()
    lights.map( light => Ok(Json.toJson(light)))
  }

  def deleteLight(id: Long): Action[AnyContent] = SecuredAction {
    lightsRepo.delete(id)
    Redirect("/lights")
  }

  def updateLight(id: Long): Action[AnyContent] = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    updateLightForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      light => {
        lightsRepo.update(id, Light(light.id, light.name, light.price)).map { _ =>
          Redirect(routes.LightController.getLights()).flashing("success" -> "light updated")
        }
      }
    )
  }

  def addLight = SecuredAction.async { implicit request =>
    lightForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      light => {
        lightsRepo.create(light.name, light.price).map { _ =>
          Redirect(routes.LightController.getLights()).flashing("success" -> "light created")
        }
      }
    )
  }
}

case class CreateLightForm(name: String, price: BigDecimal)
case class UpdateLightForm(id: Long, name: String, price: BigDecimal)
