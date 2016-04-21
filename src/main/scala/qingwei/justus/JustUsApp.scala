package qingwei.justus

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import qingwei.justus.auth.{ AuthManager, AuthRoute }
import qingwei.justus.logging.JustUsLogger
import qingwei.justus.post.PostRoute
import spray.json.DefaultJsonProtocol
import qingwei.justus.postgres.CustomPostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import ch.megard.akka.http.cors.CorsDirectives._

object JustUsApp extends App with PostRoute with AuthRoute with AuthManager with DefaultJsonProtocol {
  val db: Database = Database.forConfig("postgres")

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val allRoute = authRoute ~ postRoute
  val corsRoute = cors()(allRoute)
  val loggedRoute = JustUsLogger.log(Logging.DebugLevel, corsRoute)

  Http().bindAndHandle(loggedRoute, "localhost", 9000)
}
