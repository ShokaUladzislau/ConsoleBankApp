package Models

import java.io.Serializable

internal enum class userRole {
    Customer, Employee
}

class User : Serializable {
    var userId = 0
    var username: String? = null
    var password: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var userRole: String? = null
        set(userRole) {
            val role = Models.userRole.valueOf(userRole!!)
            field = role.name
        }
}