package controllers

import com.mohiva.play.silhouette.api.actions.SecuredRequest

import javax.inject._
import models.{Bed, BedRepository}
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
class BedController @Inject()(bedsRepo: BedRepository, cc: MessagesControllerComponents, scc: SilhouetteControllerComponents)(implicit ec: ExecutionContext) extends SilhouetteController(scc) {

  val bedForm: Form[CreateBedForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(CreateBedForm.apply)(CreateBedForm.unapply)
  }

  val updateBedForm: Form[UpdateBedForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(UpdateBedForm.apply)(UpdateBedForm.unapply)
  }

  def getBeds: Action[AnyContent] = SecuredAction.async { implicit request =>
    val beds = bedsRepo.list()
    beds.map( bed => Ok(Json.toJson(bed)))
  }

  def deleteBed(id: Long): Action[AnyContent] = SecuredAction {
    bedsRepo.delete(id)
    Redirect("/beds")
  }

  def updateBed(id: Long): Action[AnyContent] = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    updateBedForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      bed => {
        bedsRepo.update(id, Bed(bed.id, bed.name, bed.price)).map { _ =>
          Redirect(routes.BedController.getBeds()).flashing("success" -> "bed updated")
        }
      }
    )
  }

  def addBed = SecuredAction.async { implicit request =>
    bedForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      bed => {
        bedsRepo.create(bed.name, bed.price).map { _ =>
          Redirect(routes.BedController.getBeds()).flashing("success" -> "bed created")
        }
      }
    )
  }
}

case class CreateBedForm(name: String, price: BigDecimal)
case class UpdateBedForm(id: Long, name: String, price: BigDecimal)
