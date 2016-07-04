package controllers
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import models.{CampaignsDAO, DeliveryLogsDAO}
import play.api.mvc._

class ReportController extends Controller {

  def byDate = Action {
    Ok(views.html.Report.ByDate())
  }

  def byCampaign = Action.async { implicit rs =>
    DeliveryLogsDAO.getSumGroupByCampaign.map { records => Ok(views.html.Report.ByCampaign(records)) }
  }

}
