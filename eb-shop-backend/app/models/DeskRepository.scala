package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DeskRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class DeskTable(tag: Tag) extends Table[Desk](tag, "desk") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def price = column[BigDecimal]("price")
    def * = (id, name, price) <> ((Desk.apply _).tupled, Desk.unapply)
  }

  private val desk = TableQuery[DeskTable]

  def create(name: String, price: BigDecimal): Future[Desk] = db.run {
    (desk.map(c => (c.name, c.price))
      returning desk.map(_.id)
      into {case ((name,price),id) => Desk(id,name, price)}
      ) += (name, price)
  }

  def list(): Future[Seq[Desk]] = db.run {
    desk.result
  }

  def delete(id: Long): Future[Unit] = db.run(desk.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newDesk: Desk): Future[Unit] = {
    val deskToUpdate: Desk = newDesk.copy(id)
    db.run(desk.filter(_.id === id).update(deskToUpdate)).map(_ => ())
  }
}