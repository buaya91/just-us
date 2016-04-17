package qingwei.justus.post

import java.time.LocalDate

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import slick.driver.PostgresDriver.api._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import qingwei.justus.auth.AuthManager
import qingwei.justus.post.model.{ BlogPost, PostTable, UserSubmitPost, UserSubmitPostUpdate }
import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

trait PostRoute extends PostSprayJson with SprayJsonSupport {
  this: DefaultJsonProtocol with AuthManager =>

  def db: Database

  def insertPost(post: BlogPost): Future[Long] = db.run(PostTable.insertPost(post))
  def updatePost(update: UserSubmitPostUpdate): Future[Boolean] =
    db.run(PostTable.updatePost(update)).map(rc => if (rc == 1) true else false)

  def getPostById(id: Long): Future[BlogPost] = db.run(PostTable.getById(id)).map(s => s.head)
  def getPostByTag(tag: String): Future[Seq[BlogPost]] = db.run(PostTable.getByTag(tag))
  def getPostByDateRange(before: Option[LocalDate], after: Option[LocalDate]): Future[Seq[BlogPost]] =
    db.run(PostTable.getByDateRange(before, after))
  def getPostByAuthor(author: String): Future[Seq[BlogPost]] = db.run(PostTable.getByAuthor(author))

  val postRoute = path("post") {
    post {
      entity(as[UserSubmitPost]) { post =>
        requiredSession(oneOff, usingHeaders) { session =>
          val generatePost = BlogPost(session.id, post.title, post.content, post.postAt, post.tags)
          onComplete(insertPost(generatePost)) {
            case Success(pid) => complete(OK, Map("pid" -> pid))
            case Failure(e) => complete(InternalServerError, e.getLocalizedMessage())
          }
        }
      }
    } ~
      get {
        parameter('id.as[Long]) { id =>
          onComplete(getPostById(id)) {
            case Success(post) => complete(OK, post)
            case Failure(_) => complete(BadRequest, "Post not found")
          }
        } ~
          parameters('tag) { tag =>
            onComplete(getPostByTag(tag)) {
              case Success(posts) => complete(OK, posts)
              case Failure(e) => complete(InternalServerError, e.getLocalizedMessage())
            }
          } ~
          parameter('author) { author =>
            onComplete(getPostByAuthor(author)) {
              case Success(posts) => complete(OK, posts)
              case Failure(e) => complete(InternalServerError, e.getLocalizedMessage())
            }
          } ~
          parameterMultiMap { params =>
            val before = params.get("before").flatMap(l => l.headOption).map(dateString => LocalDate.parse(dateString))
            val after = params.get("after").flatMap(l => l.headOption).map(dateString => LocalDate.parse(dateString))
            onComplete(getPostByDateRange(before, after)) {
              case Success(posts) => complete(OK, posts)
              case Failure(e) => complete(InternalServerError, e.getLocalizedMessage())
            }
          }
      } ~
      path("modify") {
        post {
          entity(as[UserSubmitPostUpdate]) { update =>
            requiredSession(oneOff, usingHeaders) { session =>
              onComplete(updatePost(update)) {
                case Success(true) => complete(OK)
                case Success(false) => complete(BadRequest, "Update fail: Post does not exist")
                case Failure(e) => complete(InternalServerError, e.getLocalizedMessage())
              }
            }
          }
        }
      }
  }
}
