package controllers

import play.api.mvc._
import models._
import java.sql.Timestamp
import java.util.Date

import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.Future

class SampleController extends Controller {

  def index = Action {

    val result = DeliveryLogsDAO.getSumGroupByCampaign
//    println(result)
    Await.result(result, Duration.Inf) foreach println


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




