package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DrawerRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class DrawerTable(tag: Tag) extends Table[Drawer](tag, "drawer") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def price = column[BigDecimal]("price")
    def * = (id, name, price) <> ((Drawer.apply _).tupled, Drawer.unapply)
  }

  private val drawer = TableQuery[DrawerTable]

  def create(name: String, price: BigDecimal): Future[Drawer] = db.run {
    (drawer.map(c => (c.name, c.price))
      returning drawer.map(_.id)
      into {case ((name,price),id) => Drawer(id,name, price)}
      ) += (name, price)
  }

  def list(): Future[Seq[Drawer]] = db.run {
    drawer.result
  }

  def delete(id: Long): Future[Unit] = db.run(drawer.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newDrawer: Drawer): Future[Unit] = {
    val drawerToUpdate: Drawer = newDrawer.copy(id)
    db.run(drawer.filter(_.id === id).update(drawerToUpdate)).map(_ => ())
  }
}