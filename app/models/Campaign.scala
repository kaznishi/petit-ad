package models

import play.api.Play
import slick.driver.H2Driver.api._

import scala.concurrent.Future
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile

case class Campaign(id: Option[Int], title: String, content: String)

trait CampaignsTable {
  class Campaigns(tag: Tag) extends Table[Campaign](tag, "CAMPAIGNS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def title = column[String]("TITLE")
    def content = column[String]("CONTENT")
    def * = (id.?, title, content) <> (Campaign.tupled, Campaign.unapply)
  }
  val campaigns = TableQuery[Campaigns]
}

object CampaignsDAO extends HasDatabaseConfig[JdbcProfile] with CampaignsTable {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  def findById(id: Int): Future[Option[Campaign]] =
    db.run(campaigns.filter(_.id === id).result.headOption)

  def findAll: Future[Seq[Campaign]] =
    db.run(campaigns.result)

  def insert(newRecord: Campaign): Future[Int] = {
    db.run((campaigns returning campaigns.map(_.id)) += newRecord)
  }

  def update(record: Campaign): Future[Int] = {
    db.run(campaigns.filter(_.id === record.id)
      .map(a => (a.title, a.content))
      .update((record.title, record.content)))
  }

  def getCampaignIds: Future[Seq[Int]] = {
    db.run(campaigns.map(row => (row.id)).result)
  }


}
