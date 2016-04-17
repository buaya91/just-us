package qingwei.justus.auth

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FunSpec, ShouldMatchers}
import qingwei.justus.IntegrationHelper
import spray.json.DefaultJsonProtocol

class AuthRouteSpec extends FunSpec
  with ShouldMatchers
  with ScalatestRouteTest
  with AuthRoute
  with AuthManager
  with DefaultJsonProtocol
  with IntegrationHelper
{
  describe("POST /login") {
    it("should return unmatch error when email password does not match") {
      Post("/login", testCredential) ~> authRoute ~> check {
        status should be (StatusCodes.OK)
        header("Set-Authorization") should not be empty
      }
    }
  }
}
