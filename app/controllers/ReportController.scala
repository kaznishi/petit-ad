package controllers

import play.api.mvc._

class ReportController extends Controller {

  def byDate = Action {
    Ok(views.html.Report.ByDate())
  }

  def byCampaign = Action {
    Ok(views.html.Report.ByCampaign())
  }

}
