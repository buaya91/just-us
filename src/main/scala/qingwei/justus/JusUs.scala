package qingwei.justus

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import qingwei.justus.auth.{ AuthManager, AuthRoute }
import qingwei.justus.post.PostRoute
import spray.json.DefaultJsonProtocol
import qingwei.justus.postgres.CustomPostgresDriver.api._

object JusUs extends App with PostRoute with AuthRoute with AuthManager with DefaultJsonProtocol {
  val db: Database = Database.forConfig("postgres")

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  Http().bindAndHandle(authRoute ~ postRoute, "localhost", 9000)
}
