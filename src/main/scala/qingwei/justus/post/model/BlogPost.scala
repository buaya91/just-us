package qingwei.justus.post.model

import java.time.LocalDate

import qingwei.justus.auth.model.UserTable
import qingwei.justus.postgres.CustomPostgresDriver.api._

case class BlogPost(author: String, title: String, content: String, postAt: LocalDate, tags: List[String])

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

  def * = (author, title, content, postAt, tags) <> (BlogPost.tupled, BlogPost.unapply)
}

object PostTable {
  val allPost = TableQuery[PostTable]

  def getById(pid: Long) =
    allPost.filter(p => p.pid === pid).result

  def getByAuthor(author: String) =
    allPost.filter(p => p.author === author).result

  def getByTag(tag: String) =
    allPost.filter(p => p.tags @> List(tag)).result

  def getByDateRange(before: Option[LocalDate], after: Option[LocalDate]) = {
    val query = (before, after) match {
      case (Some(bf), Some(aft)) => allPost.filter(p => p.postAt >= aft && p.postAt <= bf)
      case (Some(bf), None) => allPost.filter(p => p.postAt <= bf)
      case (None, Some(aft)) => allPost.filter(p => p.postAt >= aft)
      case _ => allPost
    }
    query.result
  }

  def insertPost(post: BlogPost) =
    (allPost returning allPost.map(_.pid)) += post

  def updatePost(update: UserSubmitPostUpdate) = {
    val post = allPost.filter(p => p.pid === update.pid && p.author === update.author)
    (update.title, update.content) match {
      case (Some(t), Some(c)) => post.map(p => (p.title, p.content)).update((t, c))
      case (Some(t), None)    => post.map(_.title).update(t)
      case (None, Some(c))    => post.map(_.content).update(c)
      case _                  => DBIO.successful(0)
    }
  }
}