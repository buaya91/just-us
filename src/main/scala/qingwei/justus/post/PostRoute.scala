package qingwei.justus.post

import java.time.LocalDate

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import qingwei.justus.auth.AuthManager
import qingwei.justus.postgres.CustomPostgresDriver.api._
import qingwei.justus.post.model.{ BlogPost, UserSubmitPost, UserSubmitPostUpdate }
import spray.json.DefaultJsonProtocol

import qingwei.justus.post.PostController._
import scala.util.{ Failure, Success }

trait PostRoute extends PostSprayJson with SprayJsonSupport {
  this: DefaultJsonProtocol with AuthManager =>

  implicit def db: Database

  val postRoute = pathPrefix("post") {
    path(LongNumber) { pid =>
      post {
        entity(as[UserSubmitPost]) { post =>
          requiredSession(oneOff, usingHeaders) { session =>
            val update = UserSubmitPostUpdate(session.id, pid, post.title, post.content, post.tags)
            onComplete(updatePost(update)) {
              case Success(true) => complete(OK)
              case Success(false) => complete(BadRequest, "Update fail: Post does not exist")
              case Failure(e) => complete(InternalServerError, e.getLocalizedMessage())
            }
          }
        }
      }
    } ~
      post {
        entity(as[UserSubmitPost]) { post =>
          requiredSession(oneOff, usingHeaders) { session =>
            val generatePost = BlogPost(session.id, post.title, post.content, LocalDate.now(), post.tags)
            onComplete(insertPost(generatePost)) {
              case Success(pid) => complete(OK, Map("pid" -> pid))
              case Failure(e) => complete(InternalServerError, e.getLocalizedMessage())
            }
          }
        }
      } ~
      get {
        parameter('id.as[Long]) { id =>
          onComplete(getById(id)) {
            case Success(post) => complete(OK, post)
            case Failure(_) => complete(BadRequest, "Post not found")
          }
        } ~
          parameters('tag) { tag =>
            onComplete(getByTag(tag)) {
              case Success(posts) => complete(OK, posts)
              case Failure(e) => complete(InternalServerError, e.getLocalizedMessage())
            }
          } ~
          parameter('author) { author =>
            onComplete(getByAuthor(author)) {
              case Success(posts) => complete(OK, posts)
              case Failure(e) => complete(InternalServerError, e.getLocalizedMessage())
            }
          } ~
          onComplete(getAll) {
            case Success(posts) => complete(OK, posts)
            case Failure(e) => complete(InternalServerError, e.getLocalizedMessage())
          }
      }
  }
}
