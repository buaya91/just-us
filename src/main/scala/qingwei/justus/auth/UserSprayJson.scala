package qingwei.justus.auth

import qingwei.justus.auth.model.User
import spray.json.DefaultJsonProtocol

trait UserSprayJson {
  this: DefaultJsonProtocol =>
  implicit val userToJson = jsonFormat3(User)
}
