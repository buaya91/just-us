package qingwei.justus.post

import qingwei.justus.auth.AuthController
import qingwei.justus.post.model.{BlogPost, PostTable, UserSubmitPostUpdate}
import qingwei.justus.postgres.CustomPostgresDriver.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object PostController {
  val allPost = TableQuery[PostTable]

  def insertPost(post: BlogPost)(implicit db: Database): Future[Long] =
    db.run((allPost returning allPost.map(_.pid)) += post)

  def updatePost(update: UserSubmitPostUpdate)(implicit db: Database): Future[Boolean] = {
    val post = allPost.filter(p => p.pid === update.pid && p.author === update.author)
    val updateResult = (update.title, update.content) match {
      case (Some(t), Some(c)) => post.map(p => (p.title, p.content)).update((t, c))
      case (Some(t), None) => post.map(_.title).update(t)
      case (None, Some(c)) => post.map(_.content).update(c)
      case _ => DBIO.successful(0)
    }
    db.run(updateResult).map(rc => if (rc == 1) true else false)
  }

  def getById(pid: Long)(implicit db: Database): Future[Option[BlogPost]] = {
    val postJoinAuthorName = (
      (allPost join AuthController.allUser).on(_.author === _.email)
        .filter { case (p, u) => p.pid === pid }
        .map { case (p, u) => (p, u.name)}
    ).result

    db.run(postJoinAuthorName).map(_.headOption.map {
      case (p, u) => BlogPost(p.pid, u, p.title, p.content, p.postAt, p.tags)
    })
  }

  def getAll(implicit db: Database): Future[Seq[BlogPost]] = {
    val postJoinAuthorName = (
      (allPost join AuthController.allUser).on(_.author === _.email)
        .map { case (p, u) => (p, u.name)}
      ).result

    db.run(postJoinAuthorName).map(_.map {
      case (p, u) => BlogPost(p.pid, u, p.title, p.content, p.postAt, p.tags)
    })
  }

  def getByTag(tag: String)(implicit db: Database): Future[Seq[BlogPost]] = {
    val postJoinAuthorName = (
      (allPost join AuthController.allUser).on(_.author === _.email)
        .filter { case (p, u) => p.tags @> List(tag) }
        .map { case (p, u) => (p, u.name)}
      ).result

    db.run(postJoinAuthorName).map(_.map {
      case (p, u) => BlogPost(p.pid, u, p.title, p.content, p.postAt, p.tags)
    })
  }

  def getByAuthor(author: String)(implicit db: Database): Future[Seq[BlogPost]] = {
    val postJoinAuthorName = (
      (allPost join AuthController.allUser).on(_.author === _.email)
        .filter { case (p, u) => u.email === author }
        .map { case (p, u) => (p, u.name)}
      ).result

    db.run(postJoinAuthorName).map(_.map {
      case (p, u) => BlogPost(p.pid, u, p.title, p.content, p.postAt, p.tags)
    })
  }


}
