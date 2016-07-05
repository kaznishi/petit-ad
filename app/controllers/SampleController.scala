package controllers

import play.api.mvc._
import models._
import java.sql.Timestamp
import java.util.Date

import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.Future

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.ext._
import com.github.tototoshi.play2.json4s.native._

class SampleController extends Controller with Json4s {
  implicit val formats = DefaultFormats

  def index = Action {

    val result = DeliveryLogsDAO.getSumGroupByCampaign
//    println(result)
    Await.result(result, Duration.Inf) foreach println


    Ok(views.html.Sample.index("index page."))
  }

  def jsontest = Action { implicit request =>
    Ok(Extraction.decompose(Task(1, "nametest")))
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




