package BusinessLogic

import Exceptions.UserException
import Logger.MyLogger
import Models.Account
import Models.User
import Storages.AccountStorage
import Storages.TransactionStorage
import Storages.UserStorage
import userActions.CustomerActions
import userActions.EmployeeActions
import java.util.*

class BankSystem
//make the constructor private so that this class cannot be
//instantiated
private constructor() {
    var accountStorage = AccountStorage()
    var userStorage = UserStorage(accountStorage)
    var transactionStorage = TransactionStorage(accountStorage)
    var customerActions = CustomerActions(this, accountStorage, userStorage, transactionStorage)
    var employeeActions = EmployeeActions(this, accountStorage, userStorage, transactionStorage)
    var scanner = Scanner(System.`in`)
    fun showMenu() {
        println("Welcome in the bank system!")
        userStorage.deserializeUsers()
        accountStorage.deserializeAccounts()
        transactionStorage.deserializeTransactions()
        showMainMenu()
    }

    fun showMainMenu() {
        println(
            """
    Please choice your operation
    R - Registration
    L - Login
    E - Exit
    """.trimIndent()
        )
        val choice = input()
        while (choice !== "E") {
            when (choice) {
                "R" -> {
                    MyLogger.writeInLog("User registration process")
                    registerNewUser()
                }
                "L" -> {
                    MyLogger.writeInLog("User login process")
                    loginUser()
                }
                "E" -> {
                    MyLogger.writeInLog("Terminate program")
                    userStorage.serializeUsers()
                    accountStorage.serializeAccounts()
                    transactionStorage.serializeTransactions()
                    System.exit(0)
                }
                else -> {
                    MyLogger.writeInLog("Unknown command in main menu")
                    println("Wrong option")
                    showMainMenu()
                }
            }
            showMainMenu()
        }
    }

    fun showUserInformation(user: User, account: Account) {
        println(
            "| User ID - " + user.userId + " " +
                    "| User Login - " + user.username + " " +
                    "| User Password - " + user.password + " " +
                    "| User Firstname - " + user.firstName + " " +
                    "| User Lastname - " + user.lastName + " " +
                    "| User email - " + user.email + " " +
                    "| User Role - " + user.userRole
        )
        println(
            "| Account ID - " + account.accountId + " " +
                    "| Account Balance - " + account.balance + " " +
                    "| Account Status - " + account.accountStatus
        )
    }

    fun showUserInformation(user: User) {
        println(
            "| User ID - " + user.userId + " " +
                    "| User Login - " + user.username + " " +
                    "| User Password - " + user.password + " " +
                    "| User Firstname - " + user.firstName + " " +
                    "| User Lastname - " + user.lastName + " " +
                    "| User email - " + user.email + " " +
                    "| User Role - " + user.userRole
        )
    }

    private fun loginUser() {
        println("*** LogIn ***")
        var user: User? = null
        print("Write you username - ")
        val userName = input()
        print("Write you password - ")
        val password = input()
        try {
            user = userStorage.getUserByUsernameAndPass(userName, password)
        } catch (uex: UserException) {
            println(uex.message)
        }
        if (user != null && user.userRole == "Customer") {
            println("Welcome! " + user.firstName + " " + user.lastName)
            customerActions.viewUserMenu(user)
        } else if (user != null && user.userRole == "Employee") {
            println("Welcome! " + user.firstName + " " + user.lastName)
            employeeActions.viewUserMenu(user)
        }
    }

    private fun registerNewUser() {
        val user = User()
        println("*** Registration ***")
        println(
            """
    Chioce type of user:
    C - Customer
    E - Employee
    M - Back to previous menu
    """.trimIndent()
        )
        val choice = input()
        while (choice !== "M") {
            when (choice) {
                "C" -> {
                    setUserInfo(user)
                    MyLogger.writeInLog("Register customer in register menu")
                    userStorage.addUser(user, "Customer")
                    showMainMenu()
                }
                "E" -> {
                    setUserInfo(user)
                    MyLogger.writeInLog("Register employee in register menu")
                    userStorage.addUser(user, "Employee")
                    showMainMenu()
                }
                "M" -> {
                    MyLogger.writeInLog("Back to main menu")
                    showMainMenu()
                }
                else -> {
                    MyLogger.writeInLog("Unknown command in register menu")
                    println("Wrong option")
                    showMainMenu()
                }
            }
        }
    }

    private fun setUserInfo(user: User) {
        print("Write you username - ")
        user.username = input()
        print("Write you password - ")
        user.password = input()
        print("Write you firstname - ")
        user.firstName = input()
        print("Write you lastname - ")
        user.lastName = input()
        print("Write you email - ")
        user.email = input()
    }

    private fun input(): String {
        return scanner.next()
    }

    companion object {
        //Get the only object available
        //create an SingleObject
        val instance = BankSystem()
    }
}