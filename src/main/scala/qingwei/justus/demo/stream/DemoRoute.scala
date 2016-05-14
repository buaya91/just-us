package qingwei.justus.demo

import akka.stream.ActorMaterializer
import qingwei.justus.demo.stream.StreamRoute

trait DemoRoute extends StreamRoute {
  implicit val materializer: ActorMaterializer
  val demoRoute = streamHandler
}
