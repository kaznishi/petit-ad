package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.{CampaignsDAO, DeliveryLogsDAO}
import org.joda.time.DateTime
import play.api.mvc._

class ReportController extends Controller {

  def byDate = Action.async {
    val startDate = new DateTime("2016-01-01")
    val endDate = new DateTime("2016-12-31")

    DeliveryLogsDAO.getSumGroupByDate(startDate,endDate).map { records => Ok(views.html.Report.ByDate(records)) }
  }

  def byCampaign = Action.async { implicit rs =>
    val startDate = new DateTime("2016-01-01")
    val endDate = new DateTime("2016-12-31")

    DeliveryLogsDAO.getSumGroupByCampaign(startDate,endDate).map { records => Ok(views.html.Report.ByCampaign(records)) }
  }

}
