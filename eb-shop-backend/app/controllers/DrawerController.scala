package controllers

import com.mohiva.play.silhouette.api.actions.SecuredRequest

import javax.inject._
import models.{Drawer, DrawerRepository}
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
class DrawerController @Inject()(drawersRepo: DrawerRepository, cc: MessagesControllerComponents, scc: SilhouetteControllerComponents)(implicit ec: ExecutionContext) extends SilhouetteController(scc) {

  val drawerForm: Form[CreateDrawerForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(CreateDrawerForm.apply)(CreateDrawerForm.unapply)
  }

  val updateDrawerForm: Form[UpdateDrawerForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "price" -> bigDecimal,
    )(UpdateDrawerForm.apply)(UpdateDrawerForm.unapply)
  }

  def getDrawers: Action[AnyContent] = SecuredAction.async { implicit request =>
    val drawers = drawersRepo.list()
    drawers.map( drawer => Ok(Json.toJson(drawer)))
  }

  def deleteDrawer(id: Long): Action[AnyContent] = SecuredAction {
    drawersRepo.delete(id)
    Redirect("/drawers")
  }

  def updateDrawer(id: Long): Action[AnyContent] = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    updateDrawerForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      drawer => {
        drawersRepo.update(id, Drawer(drawer.id, drawer.name, drawer.price)).map { _ =>
          Redirect(routes.DrawerController.getDrawers()).flashing("success" -> "drawer updated")
        }
      }
    )
  }

  def addDrawer = SecuredAction.async { implicit request =>
    drawerForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest
        )
      },
      drawer => {
        drawersRepo.create(drawer.name, drawer.price).map { _ =>
          Redirect(routes.DrawerController.getDrawers()).flashing("success" -> "drawer created")
        }
      }
    )
  }
}

case class CreateDrawerForm(name: String, price: BigDecimal)
case class UpdateDrawerForm(id: Long, name: String, price: BigDecimal)