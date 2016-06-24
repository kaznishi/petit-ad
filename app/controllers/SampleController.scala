package controllers

import com.google.inject.Inject
import play.api._
import play.api.mvc._
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

class SampleController @Inject()(dbConfigProvider: DatabaseConfigProvider)  extends Controller {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  def index = Action {
    Ok(views.html.Sample.index("index page."))
  }

  def list = Action {
    val lists = List(Task(1,"hogehoge"),Task(11,"hogehoge11"),Task(21,"hogehoge21"))
    Ok(views.html.Sample.list("list page.", lists))
  }

  def hoge = Action {
    Ok(views.html.Sample.hoge("hoge title!!!"))
  }

  def test = Action {
    Ok(views.html.Sample.test("test!!!"))
  }

}

case class Task(id: Int, name: String) {

}





