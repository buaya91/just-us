package qingwei.justus.post.model

case class UserSubmitPostUpdate(author: String, pid: Long, title: Option[String], content: Option[String])
