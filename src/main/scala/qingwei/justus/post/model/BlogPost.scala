package qingwei.justus.post.model

import java.time.LocalDate

import qingwei.justus.auth.model.UserTable
import qingwei.justus.postgres.CustomPostgresDriver.api._

case class BlogPost(pid: Long, author: String, title: String, content: String, postAt: LocalDate, tags: List[String])
object BlogPost {
  def apply(author: String, title: String, content: String, postAt: LocalDate, tags: List[String]): BlogPost =
    new BlogPost(0L, author, title, content, postAt, tags)

  def tupled(t: (Long, String, String, String, LocalDate, List[String])) =
    new BlogPost(t._1, t._2, t._3, t._4, t._5, t._6)
}

case class UserSubmitUpdate()

class PostTable(tag: Tag) extends Table[BlogPost](tag, "posts") {
  def pid = column[Long]("pid", O.PrimaryKey, O.AutoInc)
  def author = column[String]("author") // fk of user table
  def title = column[String]("title")
  def content = column[String]("content")
  def postAt = column[LocalDate]("post_at")
  def tags = column[List[String]]("tags")

  def userFK =
    foreignKey("author_fk", author, TableQuery[UserTable])(_.email, ForeignKeyAction.Restrict, ForeignKeyAction.Cascade)

  def * = (pid, author, title, content, postAt, tags) <> (BlogPost.tupled, BlogPost.unapply)
}
