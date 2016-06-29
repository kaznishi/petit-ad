package controllers

import models._
import play.api.data._
import play.api.data.Forms._

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._


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
    Ok(views.html.Campaign.Add())
  }

  def addPost = Action.async { implicit rs =>
    campaignForm.bindFromRequest.fold(
      error => {
        CampaignsDAO.findById(1).map { _ =>
          BadRequest(views.html.Campaign.Add())
        }
      },
      form => {
        CampaignsDAO.findById(1).map { _ =>
          Redirect(routes.CampaignController.list)
        }
      }
    )
  }

  def edit = Action {
    Ok(views.html.Campaign.Edit())
  }

}
