package userActions

import Models.User

abstract class UserActions {
    abstract fun viewUserMenu(user: User)
}