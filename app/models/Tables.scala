package models

import slick.driver.H2Driver.api._

case class Campaign(id: Int, title: String, content: String)

class Campaigns(tag: Tag) extends Table[Campaign](tag, "CAMPAIGNS") {
  def id = column[Int]("ID", O.PrimaryKey)
  def title = column[String]("TITLE")
  def content = column[String]("CONTENT")
  def * = (id, title, content) <> (Campaign.tupled, Campaign.unapply)
}


