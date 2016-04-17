package qingwei.justus.auth.model

import com.github.t3hnar.bcrypt._
import qingwei.justus.postgres.CustomPostgresDriver.api._

case class User(email: String, password: String, name: String) {
  def hashed: User = User(email, password.bcrypt, name)
}

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def email = column[String]("email", O.PrimaryKey)
  def password = column[String]("password")
  def name = column[String]("name")
  def * = (email, password, name) <> (User.tupled, User.unapply)
}

object UserTable {
  val allUser = TableQuery[UserTable]

  def getPasswordHash(email: String) =
    allUser.filter(u => u.email === email).map(_.password).result

  def getName(email: String) =
    allUser.filter(u => u.email === email).map(_.name).result
}
