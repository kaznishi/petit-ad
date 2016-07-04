package controllers

import models._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import scala.util.Random
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object DeliveryController {
  case class Delivery(title: String, content: String)
  object Delivery {
    def apply(a: Campaign) = {
      new Delivery(a.title, a.content)
    }
  }

  implicit val deliveryWrites = (
    (__ \ "title").write[String] and (__ \ "content").write[String]
    )(unlift(Delivery.unapply))

}

class DeliveryController extends Controller {
  import DeliveryController._

  def default = Action.async { implicit rs =>
    CampaignsDAO.findById(1).map {
      case Some(record) => {
        DeliveryLogsDAO.insert(DeliveryLog(None, record.id.get, new DateTime(), new DateTime()))
        Ok(Json.obj("data" -> Delivery(record)))
      }
      case None =>
        Ok(Json.obj("data" -> Delivery("no title", "no content")))
    }
  }

  def random = Action.async { implicit rs =>
    CampaignsDAO.getCampaignIds.flatMap {
      case ids => {
        val r = new Random
        val index = r.nextInt().abs % ids.length

        CampaignsDAO.findById(ids(index)).map {
          case Some(record) => {
            DeliveryLogsDAO.insert(DeliveryLog(None, record.id.get, new DateTime(), new DateTime()))
            Ok(Json.obj("data" -> Delivery(record)))
          }
          case None =>
            Ok(Json.obj("data" -> Delivery("no title", "no content")))
        }
      }
    }
  }

  /**
    * DeliveryLogsのinsertをmapを使って同期処理完了後に画面遷移するように変更してみたもの
    * @return
    */
  def random2 = Action.async { implicit rs =>
    CampaignsDAO.getCampaignIds.flatMap {
      case ids => {
        val r = new Random
        val index = r.nextInt().abs % ids.length

        CampaignsDAO.findById(ids(index)).flatMap {
          case Some(record) => {
            DeliveryLogsDAO.insert(DeliveryLog(None, record.id.get, new DateTime(), new DateTime())).map { r =>
              Ok(Json.obj("data" -> Delivery(record)))
            }
          }
          case None =>
            Future.successful(Ok(Json.obj("data" -> Delivery("no title", "no content"))))

        }
      }
    }

  }

  /**
    * random2をforを使って書き直したもの
    * @return
    */
  def random3 = Action.async { implicit rs =>
    for {
      ids <- CampaignsDAO.getCampaignIds
      optRecord <- {
        val r = new Random
        val index = r.nextInt().abs % ids.length

        CampaignsDAO.findById(ids(index))
      }
      r <- {
        optRecord match {
          case Some(record) => DeliveryLogsDAO.insert(DeliveryLog(None, record.id.get, new DateTime(), new DateTime()))
          case None => Future.successful(None)
        }
      }
    } yield {
      optRecord match {
        case Some(record) => Ok(Json.obj ("data" -> Delivery (record) ) )
        case None => Ok(Json.obj("data" -> Delivery("no title", "no content")))
      }
    }
  }

}
