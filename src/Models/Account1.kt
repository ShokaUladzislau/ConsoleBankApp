package Models

import java.io.Serializable

internal enum class accountStatus {
    Unapproved, Approved, Blocked
}

class Account : Serializable {
    var accountId = 0
    var balance = 0.0
    var accountStatus: String? = null
        set(accountStatus) {
            val status = Models.accountStatus.valueOf(accountStatus!!)
            field = status.name
        }
}