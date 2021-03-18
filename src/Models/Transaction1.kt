package Models

import java.io.Serializable
import java.time.LocalDateTime

internal enum class typeOfTransaction {
    Deposit, Withdraw, Transfer, Fraud
}

class Transaction : Serializable {
    var transactionId = 0
    var senderId = 0
    var recipientId = 0
    var amount = 0.0
    var typeOfTransaction: String? = null
        set(typeOfTransaction) {
            val transaction = Models.typeOfTransaction.valueOf(typeOfTransaction!!)
            field = transaction.name
        }
    var dateAndTimeOfTransaction: LocalDateTime? = null
}