package models

import play.api.libs.json.Json

case class Bed(id: Long, name: String, price: BigDecimal)

object Bed {
  implicit val bedFormat = Json.format[Bed]
}

