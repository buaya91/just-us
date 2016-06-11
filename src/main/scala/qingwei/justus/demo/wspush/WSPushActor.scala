package qingwei.justus.demo.wspush

import akka.stream.actor.ActorPublisher

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Random

class WSPushActor extends ActorPublisher[String] {

  val tick = context.system.scheduler.schedule(1 second, 1 second, self, "Lucky number")
  val cancelAfter1Min = context.system.scheduler.scheduleOnce(1 minutes, self, "Cancel")

  override def receive: Receive = {
    case "Cancel" => {
      onNext("Ended")
      tick.cancel()
    }
    case msg: String => {
      if (isActive && totalDemand > 0) {
        val random = Random.nextInt()
        val s = s"${msg} $random"
        onNext(s)
      }
    }
  }
}
