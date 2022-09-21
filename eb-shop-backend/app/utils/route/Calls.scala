package utils.route

import play.api.mvc.Call

/**
 * Defines some common redirect calls used in authentication flow.
 */
object Calls {
  /** @return The URL to redirect to when an authentication succeeds. */

  /** @return The URL to redirect to when an authentication succeeds. */
//  def home: Call = Call("GET", "http://localhost:9000/")
  def home: Call = Call("GET", "https://e-biznes-shop-oauth.azurewebsites.net/")


//  def chairs: Call = Call("GET", "http://localhost:9000/chairs")
  def chairs: Call = Call("GET", "https://e-biznes-shop-oauth.azurewebsites.net/chairs")

  /** @return The URL to redirect to when an authentication fails. */
  def signin: Call = controllers.routes.ApplicationController.index()
}
