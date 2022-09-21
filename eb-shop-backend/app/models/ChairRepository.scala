package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class ChairRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class ChairTable(tag: Tag) extends Table[Chair](tag, "chair") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    /** The name column */
    def name = column[String]("name")
    def price = column[BigDecimal]("price")

    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Chair object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Chair case classes
     * apply and unapply methods.
     */
    def * = (id, name, price) <> ((Chair.apply _).tupled, Chair.unapply)

  }

  /**
   * The starting point for all queries on the chair table.
   */

  private val chair = TableQuery[ChairTable]

  /**
   * Create a chair with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that chair.
   */
  def create(name: String, price: BigDecimal): Future[Chair] = db.run {
    // We create a projection of just the name and price columns, since we're not inserting a value for the id column
    (chair.map(c => (c.name, c.price))
      // Now define it to return the id, because we want to know what id was generated for the chair
      returning chair.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((name,price),id) => Chair(id,name, price)}
      // And finally, insert the chair into the database
      ) += (name, price)
  }

  def list(): Future[Seq[Chair]] = db.run {
    chair.result
  }

  def delete(id: Long): Future[Unit] = db.run(chair.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newChair: Chair): Future[Unit] = {
    val chairToUpdate: Chair = newChair.copy(id)
    db.run(chair.filter(_.id === id).update(chairToUpdate)).map(_ => ())
  }
}

