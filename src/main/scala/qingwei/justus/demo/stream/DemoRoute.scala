package qingwei.justus.demo

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import qingwei.justus.demo.stream.StreamRoute

trait DemoRoute extends StreamRoute {
  implicit val materializer: ActorMaterializer
  val testRoute = path("test") {
    complete("Halo, test OK")
  }
  val demoRoute = testRoute ~ streamHandler
}
