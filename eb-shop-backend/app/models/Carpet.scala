package models

import play.api.libs.json.Json

case class Carpet(id: Long, name: String, price: BigDecimal)

object Carpet {
  implicit val carpetFormat = Json.format[Carpet]
}

