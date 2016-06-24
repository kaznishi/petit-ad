package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.H2Driver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
//  lazy val schema = Companies.schema ++ Users.schema
  lazy val schema = Campaigns.schema ++ DeliveryLogs.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Campaigns
    *  @param id Database column ID SqlType(INTEGER), PrimaryKey
    *  @param title Database column TITLE SqlType(VARCHAR)
    *  @param content Database column CONTENT SqlType(TEXT) */
  case class Campaign(id: Int, title: String, content: String)
  /** GetResult implicit for fetching Campaign objects using plain SQL queries */
  implicit def GetResultCampaign(implicit e0: GR[Int], e1: GR[String], e2: GR[String]): GR[Campaign] = GR{
    prs => import prs._
      Campaign.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table CAMPAIGNS. Objects of this class serve as prototypes for rows in queries. */
  class Campaigns(_tableTag: Tag) extends Table[Campaign](_tableTag, "CAMPAIGNS") {
    def * = (id, title, content) <> (Campaign.tupled, Campaign.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(title), Rep.Some(content)).shaped.<>({r=>import r._; _1.map(_=> Campaign.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.PrimaryKey)
    /** Database column TITLE SqlType(VARCHAR) */
    val title: Rep[String] = column[String]("TITLE")
    /** Database column CONTENT SqlType(TEXT) */
    val content: Rep[String] = column[String]("CONTENT")
  }
  /** Collection-like TableQuery object for table Companies */
  lazy val Campaigns = new TableQuery(tag => new Campaigns(tag))

  /** Entity class storing rows of table DeliveryLogs
    *  @param id Database column ID SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param campaignId Database column CAMPAIGN_ID SqlType(INTEGER)
    *  @param createdAt Database column CREATED_AT SqlType(DATETIME) */
  case class DeliveryLog(id: Long, campaignId: Int, createdAt: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultDeliveryLog(implicit e0: GR[Long], e1: GR[Int], e2: GR[String]): GR[DeliveryLog] = GR{
    prs => import prs._
      DeliveryLog.tupled((<<[Long], <<[Int], <<[String]))
  }
  /** Table description of table DELIVERY_LOGS. Objects of this class serve as prototypes for rows in queries. */
  class DeliveryLogs(_tableTag: Tag) extends Table[DeliveryLog](_tableTag, "DELIVERY_LOGS") {
    def * = (id, campaignId, createdAt) <> (DeliveryLog.tupled, DeliveryLog.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(campaignId), Rep.Some(createdAt)).shaped.<>({r=>import r._; _1.map(_=> DeliveryLog.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column CAMPAIGN_ID SqlType(INTEGER) */
    val campaignId: Rep[Int] = column[Int]("CAMPAIGN_ID")
    /** Database column CREATED_AT SqlType(DATETIME) */
    val createdAt: Rep[String] = column[String]("CREATED_AT")

    /** Foreign key referencing Companies (database name IDX_USERS_FK0) */
    lazy val campaignFk = foreignKey("IDX_CAMPAIGN_FK0", campaignId, Campaigns)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Users */
  lazy val DeliveryLogs = new TableQuery(tag => new DeliveryLogs(tag))
}