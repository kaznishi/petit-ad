package models

import org.joda.time.DateTime
import play.api.Play
import slick.driver.H2Driver.api._
import com.github.tototoshi.slick.H2JodaSupport._

import scala.concurrent.Future
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.GetResult
import scala.concurrent.ExecutionContext.Implicits.global

case class DeliveryLog(id: Option[Long], campaignId: Int, date: DateTime, createdAt: DateTime)
case class DeliveryLogSummaryByCampaign(id: Int, title: String, count: Long)
case class DeliveryLogSummaryByDate(date: DateTime, count: Long)

trait DeliveryLogsTable {
  class DeliveryLogs(tag: Tag) extends Table[DeliveryLog](tag, "DELIVERY_LOGS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def campaignId = column[Int]("CAMPAIGN_ID")
    def date = column[DateTime]("DATE", O.SqlType("DATE"))
    def createdAt = column[DateTime]("CREATED_AT", O.SqlType("DATETIME"))
    def * = (id.?, campaignId, date, createdAt) <> (DeliveryLog.tupled, DeliveryLog.unapply)
  }
  val deliveryLogs = TableQuery[DeliveryLogs]
}

object DeliveryLogsDAO extends HasDatabaseConfig[JdbcProfile] with DeliveryLogsTable with CampaignsTable {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  def findById(id: Long): Future[Option[DeliveryLog]] =
    db.run(deliveryLogs.filter(_.id === id).result.headOption)

  def findAll: Future[Seq[DeliveryLog]] =
    db.run(deliveryLogs.result)

  /**
    * 指定キャンペーンのレコードを取得する
    * @param campaignId
    * @return
    */
  def findByCampaignId(campaignId: Int): Future[Seq[DeliveryLog]] =
    db.run(deliveryLogs.filter(_.campaignId === campaignId).result)

  /**
    * キャンペーン毎のレコードを取得する
    * @return
    */
  def getSumGroupByCampaign: Future[Seq[DeliveryLogSummaryByCampaign]] = {
    implicit val getDeliveryLogSummaryByCampaignResult = GetResult(r => DeliveryLogSummaryByCampaign(r.<<, r.<<, r.<<))

    db.run(sql"""
          select campaigns.id, campaigns.title, count(delivery_logs.id)
          from campaigns
          inner join delivery_logs on (campaigns.id = delivery_logs.campaign_id)
          group by delivery_logs.campaign_id
      """.as[DeliveryLogSummaryByCampaign])
  }

  /**
    * 日毎のレコード数集計を取得する
    */
  def getSumGroupByDatePlainSQL: Future[Seq[DeliveryLogSummaryByDate]] = {
    implicit val getDeliveryLogSummaryByDateResult = GetResult(r => DeliveryLogSummaryByDate(r.<<, r.<<))

    db.run(sql"""
          select delivery_logs.date, count(delivery_logs.id)
          from delivery_logs
          group by delivery_logs.date
      """.as[DeliveryLogSummaryByDate])
  }

  /**
    * 日毎のレコード数集計を取得する
    */
  def getSumGroupByDate: Future[Seq[DeliveryLogSummaryByDate]] = {
    db.run(deliveryLogs.groupBy(p => p.date).map {
      case (date, group) => (date, group.map(_.id).length)
    }.result).map {
      _.map { t: (DateTime, Int) => {DeliveryLogSummaryByDate.tupled((t._1, t._2.toLong))}}}
  }

  /**
    * 指定日のレコードを取得する
    * @param dt 集計対象日(時刻は00:00:00.0のDateTime)
    * @return
    */
  def findByDate(dt: DateTime): Future[Seq[DeliveryLog]] = {
    val nextDt = dt.plusDays(1)
    db.run(deliveryLogs.filter(_.createdAt > dt).filter(_.createdAt < nextDt).result)
  }

  def insert(newRecord: DeliveryLog): Future[Long] = {
    db.run((deliveryLogs returning deliveryLogs.map(_.id)) += newRecord)
  }
}
