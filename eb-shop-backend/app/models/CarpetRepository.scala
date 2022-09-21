package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CarpetRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class CarpetTable(tag: Tag) extends Table[Carpet](tag, "carpet") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def price = column[BigDecimal]("price")
    def * = (id, name, price) <> ((Carpet.apply _).tupled, Carpet.unapply)
  }

  private val carpet = TableQuery[CarpetTable]

  def create(name: String, price: BigDecimal): Future[Carpet] = db.run {
    (carpet.map(c => (c.name, c.price))
      returning carpet.map(_.id)
      into {case ((name,price),id) => Carpet(id,name, price)}
      ) += (name, price)
  }

  def list(): Future[Seq[Carpet]] = db.run {
    carpet.result
  }

  def delete(id: Long): Future[Unit] = db.run(carpet.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newCarpet: Carpet): Future[Unit] = {
    val carpetToUpdate: Carpet = newCarpet.copy(id)
    db.run(carpet.filter(_.id === id).update(carpetToUpdate)).map(_ => ())
  }
}