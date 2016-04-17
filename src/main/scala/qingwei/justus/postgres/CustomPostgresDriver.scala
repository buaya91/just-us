package qingwei.justus.postgres

import com.github.tminglei.slickpg._

trait CustomPostgresDriver extends ExPostgresDriver with PgDate2Support with PgSearchSupport with PgArraySupport {
  override val api = MyAPI

  object MyAPI extends API with DateTimeImplicits with SearchImplicits with ArrayImplicits
}

object CustomPostgresDriver extends CustomPostgresDriver
