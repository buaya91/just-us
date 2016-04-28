package qingwei.justus.auth

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import com.github.t3hnar.bcrypt._
import qingwei.justus.auth.model.{ Session, UserTable }
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._

import scala.collection.immutable.Map
import slick.driver.PostgresDriver.api._
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

trait AuthRoute extends UserSprayJson with SprayJsonSupport {
  this: DefaultJsonProtocol with AuthManager =>
  def db: Database

  def login(email: String, password: String): Future[Boolean] = {
    db.run(UserTable.getPasswordHash(email)).map(pws => pws.headOption match {
      case Some(pwhash) => password.isBcrypted(pwhash)
      case None => false
    })
  }

  val authRoute = path("login") {
    post {
      entity(as[Map[String, String]]) { param =>
        (param.get("username"), param.get("password")) match {
          case (Some(username), Some(password)) => onComplete(login(username, password)) {
            case Success(true) => setSession(oneOff, usingHeaders, Session(username)) { complete(OK) }
            case Success(false) => complete(BadRequest, "Username and password does not match")
            case Failure(e) => complete(InternalServerError, "Request is not completed")
          }
          case _ => complete(BadRequest, "Missing parameter")
        }
      }
    }
  }
}
