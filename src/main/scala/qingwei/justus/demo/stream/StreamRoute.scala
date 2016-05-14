package qingwei.justus.demo.stream

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{ RequestContext, Route, RouteResult }
import akka.stream.scaladsl.{ Sink, Source }
import akka.http.scaladsl.model.HttpMethods._
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.sys.process.Process

trait StreamRoute {
  implicit val materializer: ActorMaterializer

  val filePath = ConfigFactory.load().getConfig("demo.stream").getString("path")
  def logStream(): Source[ByteString, Any] = {
    val tailStream = Process(s"tail -f $filePath").lineStream.map(s => {
      println(s"stream line: $s")
      ByteString(s)
    })
    Source(tailStream)
  }

  def streamHandler: Route = (ctx: RequestContext) => {
    import RouteResult._
    ctx.request match {
      case HttpRequest(GET, Uri.Path("/demo/stream"), _, entity, _) => {
        println("receive req")
        entity.dataBytes.runWith(Sink.ignore)
        Future.successful(
          Complete(HttpResponse(entity = HttpEntity.Chunked.fromData(ContentTypes.`text/plain(UTF-8)`, logStream())))
        )
      }
      case HttpRequest(_, _, _, entity, _) => {
        entity.dataBytes.runWith(Sink.ignore)
        Future.successful(
          Complete(HttpResponse(404))
        )
      }
    }
  }
}
