package forms

import models.Bank
import play.api.libs.json.Json

object BankForm {

  case class CreateBankForm(
                           name: String,
                           bankCode: String,
                           guichetCode: String,
                           account: String,
                           ribKey: BigDecimal,
                           iban: String
                           ){
    def tobankCustom: Bank = Bank(
      id = 0L,
      name = this.name,
      bankCode = this.bankCode,
      guichetCode = this.guichetCode,
      account = this.account,
      ribKey = this.ribKey,
      iban = this.iban
    )
  }

  object CreateBankForm {
    implicit val reader = Json.reads[CreateBankForm]
  }

}
