package repositories

import models.BillWithData

import scala.concurrent.Future

trait BillRepositoryTrait {
  def getListBill: Future[Seq[BillWithData]];


}
