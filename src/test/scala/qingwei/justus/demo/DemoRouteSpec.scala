package qingwei.justus.demo

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._

class DemoRouteSpec extends FunSpec
    with DemoRoute
    with ShouldMatchers
    with ScalatestRouteTest {
  describe("Stream route") {
    it("should return chunked response") {
      Get("/demo/stream") ~> demoRoute ~> check {
        status should be(StatusCodes.OK)
      }
    }
  }
}
