package qingwei.justus.demo.wspush

import akka.NotUsed
import akka.actor.Props
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.http.scaladsl.server.Directives._
import akka.stream.FlowShape
import akka.stream.scaladsl.{ Flow, GraphDSL, Sink, Source }

trait WSPushRoute {
  def serverPushHandler: Flow[Message, Message, NotUsed] = Flow.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val ignoreInput = b.add(Sink.ignore)
    val actor = Source.actorPublisher[String](Props[WSPushActor])

    val randomOutputSource = b.add(actor)
    val stringToMsg = b.add(Flow[String].map(s => TextMessage(s)))

    randomOutputSource ~> stringToMsg

    FlowShape(ignoreInput.in, stringToMsg.out)
  })

  val wsPushRoute = path("ws-push") {
    handleWebSocketMessages(serverPushHandler)
  }
}
