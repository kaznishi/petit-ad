package controllers

import models.CampaignsDAO
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._

class CampaignController extends Controller {

  def list = Action.async {
    CampaignsDAO.findAll.map { records => Ok(views.html.Campaign.List(records)) }
  }

  def add = Action {
    Ok(views.html.Campaign.Add())
  }

  def edit = Action {
    Ok(views.html.Campaign.Edit())
  }

}
