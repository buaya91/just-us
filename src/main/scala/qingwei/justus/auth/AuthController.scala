package qingwei.justus.auth

import qingwei.justus.auth.model.UserTable
import qingwei.justus.postgres.CustomPostgresDriver.api._
import com.github.t3hnar.bcrypt._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AuthController {
  val allUser = TableQuery[UserTable]

  def getPasswordHashAndName(email: String) =
    allUser.filter(u => u.email === email).map(u => (u.password, u.name)).result

  def getName(email: String) =
    allUser.filter(u => u.email === email).map(_.name).result

  def login(email: String, password: String)(implicit db: Database ): Future[(Boolean, String)] = {
    db.run(AuthController.getPasswordHashAndName(email)).map(pws => pws.headOption match {
      case Some((pwhash, name)) => (password.isBcrypted(pwhash), name)
      case None => (false, "")
    })
  }
}
