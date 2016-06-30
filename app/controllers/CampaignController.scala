package controllers

import models._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future


object CampaignController {

  case class CampaignForm(id: Option[Int], title: String, content: String)

  val campaignForm = Form(
    mapping(
      "id" -> optional(number),
      "title" -> nonEmptyText,
      "content" -> nonEmptyText
    )(CampaignForm.apply)(CampaignForm.unapply)
  )
}

class CampaignController extends Controller {
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
        println(formWithErrors)
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

  def edit = Action {
    Ok(views.html.Campaign.Edit())
  }

}
