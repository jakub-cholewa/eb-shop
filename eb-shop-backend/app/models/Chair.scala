package models

import play.api.libs.json.Json

case class Chair(id: Long, name: String, price: BigDecimal)

object Chair {
  implicit val chairFormat = Json.format[Chair]
}