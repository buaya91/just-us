package qingwei.justus.auth

import com.softwaremill.session._
import qingwei.justus.auth.model.Session

trait AuthManager {
  implicit lazy val serializer = JValueSessionSerializer.caseClass[Session]
  implicit lazy val sessionEncoder = new JwtSessionEncoder[Session]()
  lazy val sessionConfig = SessionConfig.fromConfig()
  implicit lazy val sessionManager: SessionManager[Session] = new SessionManager[Session](sessionConfig)
}
