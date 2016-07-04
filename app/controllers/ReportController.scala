package controllers
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import models.{CampaignsDAO, DeliveryLogsDAO}
import play.api.mvc._

class ReportController extends Controller {

  def byDate = Action.async {
    DeliveryLogsDAO.getSumGroupByDate.map { records => Ok(views.html.Report.ByDate(records)) }
  }

  def byCampaign = Action.async { implicit rs =>
    DeliveryLogsDAO.getSumGroupByCampaign.map { records => Ok(views.html.Report.ByCampaign(records)) }
  }

}
