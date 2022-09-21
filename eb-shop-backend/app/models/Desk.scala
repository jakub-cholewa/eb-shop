package models

import play.api.libs.json.Json

case class Desk(id: Long, name: String, price: BigDecimal)

object Desk {
  implicit val deskFormat = Json.format[Desk]
}



