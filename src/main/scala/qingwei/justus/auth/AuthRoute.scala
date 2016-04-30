package qingwei.justus.auth

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers._

import qingwei.justus.auth.model.Session
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._

import scala.collection.immutable.Map
import slick.driver.PostgresDriver.api._
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import scala.util.{ Failure, Success }

trait AuthRoute extends UserSprayJson with SprayJsonSupport {
  this: DefaultJsonProtocol with AuthManager =>
  implicit def db: Database

  val authRoute = path("login") {
    post {
      entity(as[Map[String, String]]) { param =>
        (param.get("username"), param.get("password")) match {
          case (Some(username), Some(password)) => onComplete(AuthController.login(username, password)) {
            case Success((true, name)) => setSession(oneOff, usingHeaders, Session(username)) {
              val exposeJWT = RawHeader("Access-Control-Expose-Headers", "Set-Authorization")
              respondWithHeader(exposeJWT) {
                complete(OK, Map("name" -> name))
              }
            }
            case Success((false, _)) => complete(BadRequest, "Username and password does not match")
            case Failure(e) => complete(InternalServerError, "Request is not completed")
          }
          case _ => complete(BadRequest, "Missing parameter")
        }
      }
    }
  }
}
