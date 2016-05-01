package qingwei.justus.post.model

case class UserSubmitPostUpdate(author: String, pid: Long, title: String, content: String, tags: List[String])
