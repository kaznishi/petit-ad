package controllers

import play.api.mvc._

class CampaignController extends Controller {

  def list = Action {
    Ok(views.html.Campaign.List())
  }

  def add = Action {
    Ok(views.html.Campaign.Add())
  }

  def edit = Action {
    Ok(views.html.Campaign.Edit())
  }

}
