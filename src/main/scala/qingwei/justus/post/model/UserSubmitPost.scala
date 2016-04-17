package qingwei.justus.post.model

import java.time.LocalDate

// user does not submit author, author is obtain through session
case class UserSubmitPost(title: String, content: String, postAt: LocalDate, tags: List[String])
