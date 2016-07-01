package controllers

import com.google.inject.Inject
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.i18n.{MessagesApi, I18nSupport}

import scala.concurrent.Future


object CampaignController {

  case class CampaignFormData(title: String, content: String)

  val campaignForm = Form[CampaignFormData](
    mapping(
      "title" -> nonEmptyText,
      "content" -> nonEmptyText
    )(CampaignFormData.apply)(CampaignFormData.unapply)
  )
}

class CampaignController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  import CampaignController._

  def list = Action.async {
    CampaignsDAO.findAll.map { records => Ok(views.html.Campaign.List(records)) }
  }

  def addView = Action {
    Ok(views.html.Campaign.Add(campaignForm))
  }

  def addPost = Action.async { implicit rs =>
    campaignForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest(views.html.Campaign.Add(formWithErrors)))
      },
      formData => {
        val campaign = Campaign(None, formData.title, formData.content)
        CampaignsDAO.insert(campaign).map { _ =>
          Redirect(routes.CampaignController.list)
        }
      }
    )
  }

  def editView(id: Int) = Action.async { implicit rs =>
    CampaignsDAO.findById(id).map {
      case Some(record) => {
        val cf = campaignForm.fill(CampaignFormData(record.title, record.content))
        Ok(views.html.Campaign.Edit(id, cf))
      }
      case None =>
        Redirect(routes.CampaignController.list)
    }
  }

  def editPost(id: Int) = Action.async { implicit rs =>
    campaignForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest(views.html.Campaign.Edit(id, formWithErrors)))
      },
      formData => {
        val campaign = Campaign(Option(id), formData.title, formData.content)
        CampaignsDAO.update(campaign).map { _ =>
          Redirect(routes.CampaignController.list)
        }
      }
    )
  }
}
