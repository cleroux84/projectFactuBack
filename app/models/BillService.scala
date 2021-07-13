//package models
//
//import play.api.db.slick.HasDatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//
//trait BillService extends HasDatabaseConfigProvider[JdbcProfile] {
//  import profile.api._
//
//  class BillTable(tag: Tag) extends Table[Bill](tag, "bill") {
//    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
//    def customerId = column[Long]("customerId")
//    //      def created = column[DateTime]("created")
//    def periodCovered = column[String]("periodCovered")
//    def billNumber = column[String]("billNumber")
//    def benefit = column[String]("benefit")
//    def quantity = column[BigDecimal]("quantity")
//    def unitPrice = column[BigDecimal]("unitPrice")
//    def vatRate = column[BigDecimal]("vatRate")
//
//    def * =  (id, customerId, /*created,*/ periodCovered, billNumber, benefit, quantity, unitPrice, vatRate) <> ((Bill.apply _).tupled, Bill.unapply)
//  }
//
//  val slickBill: TableQuery[BillTable] = TableQuery[BillTable]
//
//}
//
