package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BedRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class BedTable(tag: Tag) extends Table[Bed](tag, "bed") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def price = column[BigDecimal]("price")
    def * = (id, name, price) <> ((Bed.apply _).tupled, Bed.unapply)
  }

  private val bed = TableQuery[BedTable]

  def create(name: String, price: BigDecimal): Future[Bed] = db.run {
    (bed.map(c => (c.name, c.price))
      returning bed.map(_.id)
      into {case ((name,price),id) => Bed(id,name, price)}
      ) += (name, price)
  }

  def list(): Future[Seq[Bed]] = db.run {
    bed.result
  }

  def delete(id: Long): Future[Unit] = db.run(bed.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newBed: Bed): Future[Unit] = {
    val bedToUpdate: Bed = newBed.copy(id)
    db.run(bed.filter(_.id === id).update(bedToUpdate)).map(_ => ())
  }
}

