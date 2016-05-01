package qingwei.justus.post

import java.time.LocalDate

import akka.http.scaladsl.model.{ HttpHeader, StatusCodes }
import akka.http.scaladsl.model.HttpHeader.ParsingResult
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{ BeforeAndAfterEach, FunSpec, ShouldMatchers }
import qingwei.justus.IntegrationHelper
import qingwei.justus.auth.{ AuthManager, AuthRoute }
import qingwei.justus.post.model.BlogPost
import spray.json.DefaultJsonProtocol

class PostRouteSpec extends FunSpec
    with ShouldMatchers
    with IntegrationHelper
    with ScalatestRouteTest
    with PostRoute
    with AuthManager
    with AuthRoute
    with DefaultJsonProtocol
    with BeforeAndAfterEach {

  override def beforeEach() = {
    clearPost()
    insertPost()
  }

  describe("PostRoute") {
    describe("when receive POST on /post") {
      var token = ""
      Post("/login", testCredential) ~> authRoute ~> check {
        token = header("Set-Authorization").get.value()
      }

      it("should update DB if payload is valid") {
        HttpHeader.parse("Authorization", "Bearer " + token) match {
          case ParsingResult.Ok(h, _) => {
            Post("/post", testPost).withHeaders(h) ~> postRoute ~> check {
              entityAs[Map[String, Long]].get("pid") match {
                case Some(p) => assert(true)
                case _ => assert(false, "pid not return")
              }
            }
          }
          case _ => assert(false, "no token!!")
        }
      }

      it("should return validation error if paylod is not valid") {
        HttpHeader.parse("Authorization", "Bearer " + token) match {
          case ParsingResult.Ok(h, _) => {
            Post("/post", "Simple string").withHeaders(h) ~> postRoute ~> check {
              rejection
            }
          }
          case _ => assert(false, "no token!!")
        }
      }
    }

    describe("when receive POST on /post/pid") {
      var token = ""
      Post("/login", testCredential) ~> authRoute ~> check {
        token = header("Set-Authorization").get.value()
      }

      it("should update DB if payload is valid") {
        HttpHeader.parse("Authorization", "Bearer " + token) match {
          case ParsingResult.Ok(h, _) => {
            Post("/post/0", testPost.copy(title = "Update #2")).withHeaders(h) ~> postRoute ~> check {
              status should equal(StatusCodes.OK)
            }
          }
          case _ => assert(false, "no token!!")
        }
      }

      it("should return validation error if paylod is not valid") {
        HttpHeader.parse("Authorization", "Bearer " + token) match {
          case ParsingResult.Ok(h, _) => {
            Post("/post/0", "Simple string").withHeaders(h) ~> postRoute ~> check {
              rejection
            }
          }
          case _ => assert(false, "no token!!")
        }
      }
    }

    describe("when receive GET on /post?id") {
      it("should return post with pid") {
        Get("/post?id=0") ~> postRoute ~> check {
          entityAs[BlogPost] should equal(BlogPost("Qingwei", "sample", "sample", LocalDate.now(), Nil))
        }
      }
    }

    describe("when receive GET on /post?author") {
      it("should return post with pid") {
        Get("/post?author=l.q.wei91@gmail.com") ~> postRoute ~> check {
          entityAs[Seq[BlogPost]] should contain(BlogPost("Qingwei", "sample", "sample", LocalDate.now(), Nil))
        }
      }
    }
  }
}
