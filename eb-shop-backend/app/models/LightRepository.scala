package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LightRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class LightTable(tag: Tag) extends Table[Light](tag, "light") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def price = column[BigDecimal]("price")
    def * = (id, name, price) <> ((Light.apply _).tupled, Light.unapply)
  }

  private val light = TableQuery[LightTable]

  def create(name: String, price: BigDecimal): Future[Light] = db.run {
    (light.map(c => (c.name, c.price))
      returning light.map(_.id)
      into {case ((name,price),id) => Light(id,name, price)}
      ) += (name, price)
  }

  def list(): Future[Seq[Light]] = db.run {
    light.result
  }

  def delete(id: Long): Future[Unit] = db.run(light.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newLight: Light): Future[Unit] = {
    val lightToUpdate: Light = newLight.copy(id)
    db.run(light.filter(_.id === id).update(lightToUpdate)).map(_ => ())
  }
}

