package models

import play.api.libs.json.Json

case class Light(id: Long, name: String, price: BigDecimal)

object Light {
  implicit val lightFormat = Json.format[Light]
}

