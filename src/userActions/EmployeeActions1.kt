package userActions

import BusinessLogic.BankSystem
import Exceptions.AccoutException
import Exceptions.UserException
import Logger.MyLogger
import Models.User
import Storages.AccountStorage
import Storages.TransactionStorage
import Storages.UserStorage
import java.util.*

class EmployeeActions(
    private var bankSystem: BankSystem,
    private var accountStorage: AccountStorage,
    private var userStorage: UserStorage,
    private var transactionStorage: TransactionStorage
) : UserActions() {
    private var scanner = Scanner(System.`in`)
    override fun viewUserMenu(user: User) {
        println(
            """
    Please choice your operation:
    A - Approve account
    V - View user information
    L - View a log of all transactions in chronological order
    D - Delete account
    B - Block account
    M - Back to main menu
    """.trimIndent()
        )
        when (scanner.next()) {
            "A" -> {
                MyLogger.writeInLog("Approve user account")
                approveAccount()
                viewUserMenu(user)
            }
            "V" -> {
                MyLogger.writeInLog("Viewed user information by employee - " + user.firstName + " " + user.lastName + "(" + user.username + ")")
                bankSystem.showUserInformation(user)
                viewUserMenu(user)
            }
            "L" -> {
                MyLogger.writeInLog("Show all transactions")
                transactionStorage.showAllTransactions()
                viewUserMenu(user)
            }
            "D" -> {
                MyLogger.writeInLog("Delete user account")
                deleteAccount()
                viewUserMenu(user)
            }
            "B" -> {
                MyLogger.writeInLog("Block user account")
                blockAccount()
                viewUserMenu(user)
            }
            "M" -> {
                println("B")
                bankSystem.showMainMenu()
            }
            else -> {
                MyLogger.writeInLog("Unknown command in employee menu")
                println("Wrong option")
                viewUserMenu(user)
            }
        }
    }

    private fun deleteAccount() {
        println("Write customer username")
        val customerName = scanner.next()
        val customer: User? = try {
            userStorage.getUserByUsername(customerName)
        } catch (e: UserException) {
            e.printStackTrace()
            return
        }
        userStorage.removeUser(customer)
    }

    private fun approveAccount() {
        println("Write customer username")
        val customerName = scanner.next()
        val customer: User?
        try {
            customer = userStorage.getUserByUsername(customerName)
            accountStorage.approveUserAccount(customer.userId)
        } catch (e: UserException) {
            e.printStackTrace()
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
    }

    private fun blockAccount() {
        println("Write customer username")
        val customerName = scanner.next()
        val customer: User?
        try {
            customer = userStorage.getUserByUsername(customerName)
            accountStorage.blockUserAccount(customer.userId)
        } catch (e: UserException) {
            e.printStackTrace()
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
    }
}