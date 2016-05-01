package qingwei.justus

import java.time.LocalDate

import qingwei.justus.post.model.{ BlogPost, UserSubmitPost }
import qingwei.justus.post.PostController
import qingwei.justus.postgres.CustomPostgresDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration._

trait IntegrationHelper {
  def db = Database.forConfig("postgres.test")

  val testPost = UserSubmitPost("Title", """Content should be very long and contain many things""", Nil)
  val testCredential = Map("username" -> "l.q.wei91@gmail.com", "password" -> "secret")

  val internalPost = BlogPost("l.q.wei91@gmail.com", "Some title", "Content", LocalDate.now(), Nil)

  def clearPost = () => {
    Await.result(db.run(PostController.allPost.delete), 1 second)
    db.close()
  }

  def insertPost = () => {
    val updateQ = sqlu"""insert into posts values(0, 'l.q.wei91@gmail.com', 'sample', 'sample', now(), '{}')"""
    Await.result(db.run(updateQ), 1 second)
  }

  def tryGet = () => {
    val r = Await.result(db.run(PostController.allPost.result), 1 second)
    println("rr" + r)
  }
}
