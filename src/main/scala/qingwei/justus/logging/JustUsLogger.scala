package qingwei.justus.logging

import akka.event.Logging.LogLevel
import akka.event.LoggingAdapter
import akka.http.scaladsl.server.RouteResult
import akka.http.scaladsl.model.{ HttpEntity, HttpRequest }
import akka.http.scaladsl.server.{ Route, RouteResult }
import akka.http.scaladsl.server.RouteResult.Complete
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.{ LogEntry, LoggingMagnet }
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.typesafe.scalalogging.LazyLogging
import spray.json.DefaultJsonProtocol

import scala.concurrent.{ ExecutionContext, Future }

object JustUsLogger extends LazyLogging with DefaultJsonProtocol {
  def log(level: LogLevel, route: Route)(implicit m: Materializer, ex: ExecutionContext) = {

    def entityAsString(entity: HttpEntity)(implicit m: Materializer, ex: ExecutionContext): Future[String] = {
      entity.dataBytes
        .map(_.decodeString(entity.contentType.charsetOption.get.value)).runWith(Sink.head)
    }

    def myLoggingFunction(logger: LoggingAdapter)(req: HttpRequest)(res: RouteResult): Unit = {
      val entry = res match {
        case Complete(resp) =>
          entityAsString(resp.entity).map(data ⇒ LogEntry(s"${req.method} ${req.uri}: ${resp.status} \n entity: $data", level))
        case other =>
          Future.successful(LogEntry(s"$other", level))
      }
      entry.map(_.logTo(logger))
    }
    logRequestResult(LoggingMagnet(log => myLoggingFunction(log)))(route)
  }
}
