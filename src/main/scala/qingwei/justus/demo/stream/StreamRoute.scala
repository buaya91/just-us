package qingwei.justus.demo.stream

import java.io.File

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{ RequestContext, Route, RouteResult }
import akka.stream.scaladsl.{ FileIO, Sink, Source }
import akka.http.scaladsl.model.HttpMethods._
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.concurrent.duration._

trait StreamRoute {
  implicit val materializer: ActorMaterializer

  val filePath = ConfigFactory.load().getConfig("demo.stream").getString("path")
  def logStream(): Source[ByteString, Any] = {
    FileIO.fromFile(new File(filePath)).keepAlive(2 seconds, () => ByteString(s"Server time(ms): ${System.currentTimeMillis()}"))
  }

  def timeStream: Source[ByteString, Any] =
    Source
      .repeat(ByteString("10 seconds passed"))
      .delay(10 seconds)
      .keepAlive(2 seconds, () => ByteString(s"Server time(ms): ${DateTime.now}"))

  def streamHandler: Route = (ctx: RequestContext) => {
    import RouteResult._
    ctx.request match {
      case HttpRequest(GET, Uri.Path("/demo/stream"), _, entity, _) => {
        println("receive req")
        entity.dataBytes.runWith(Sink.ignore)
        Future.successful(
          Complete(HttpResponse(entity = HttpEntity.Chunked.fromData(ContentTypes.`text/plain(UTF-8)`, timeStream)))
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
