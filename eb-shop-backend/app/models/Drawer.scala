package models

import play.api.libs.json.Json

case class Drawer(id: Long, name: String, price: BigDecimal)

object Drawer {
  implicit val drawerFormat = Json.format[Drawer]
}

