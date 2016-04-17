package qingwei.justus.post

import java.time.LocalDate

import qingwei.justus.post.model.{BlogPost, UserSubmitPost, UserSubmitPostUpdate}
import spray.json._

trait PostSprayJson {
  this: DefaultJsonProtocol =>
  implicit object LocalDateJsonFormat extends RootJsonFormat[LocalDate] {
    override def read(json: JsValue): LocalDate = LocalDate.parse(json.convertTo[String])

    override def write(obj: LocalDate): JsValue = JsString(obj.toString)
  }

  implicit val postToJson = jsonFormat5(BlogPost)
  implicit val userPostToJson = jsonFormat4(UserSubmitPost)
  implicit val userUpdateToJson = jsonFormat4(UserSubmitPostUpdate)
}
