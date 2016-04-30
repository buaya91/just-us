package qingwei.justus.auth

import qingwei.justus.auth.model.UserTable
import qingwei.justus.postgres.CustomPostgresDriver.api._

object AuthController {
  val allUser = TableQuery[UserTable]

  def getPasswordHashAndName(email: String) =
    allUser.filter(u => u.email === email).map(u => (u.password, u.name)).result

  def getName(email: String) =
    allUser.filter(u => u.email === email).map(_.name).result
}
