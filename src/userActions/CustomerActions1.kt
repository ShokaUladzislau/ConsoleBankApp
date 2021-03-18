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

class CustomerActions(
    var bankSystem: BankSystem,
    var accountStorage: AccountStorage,
    var userStorage: UserStorage,
    var transactionStorage: TransactionStorage
) : UserActions() {
    var scanner = Scanner(System.`in`)
    override fun viewUserMenu(user: User) {
        println(
            """
    
    Please choice your operation:
    V - View user information
    D - Deposit your account
    W - Withdraw your account
    S - Send money to another account
    T - View your transactions
    M - Back to main menu
    """.trimIndent()
        )
        val choice = scanner.next()
        while (choice !== "M") {
            when (choice) {
                "V" -> {
                    viewUserAccount(user)
                    viewUserMenu(user)
                }
                "D" -> {
                    depositAccount(user)
                    viewUserMenu(user)
                }
                "W" -> {
                    withdrawAccount(user)
                    viewUserMenu(user)
                }
                "S" -> {
                    transferBetweenAccounts(user)
                    viewUserMenu(user)
                }
                "T" -> viewUserTransactionsMenu(user)
                "M" -> {
                    MyLogger.writeInLog("Return to main menu")
                    bankSystem.showMainMenu()
                }
                else -> {
                    MyLogger.writeInLog("Unknown command in customer menu")
                    println("Wrong option")
                    viewUserMenu(user)
                }
            }
        }
    }

    private fun viewUserTransactionsMenu(user: User) {
        println(
            """
    
    Please choice your operation:
    U - View transactions sorted by user
    A - View transactions sorted by account
    T - View transactions sorted by transaction type
    S - View transactions sorted by transaction sum
    M - Back to user menu
    """.trimIndent()
        )
        val choice = scanner.next()
        while (choice !== "M") {
            when (choice) {
                "U" -> {
                    MyLogger.writeInLog("Show transactions by user id")
                    viewUserTransactionsByUserId(user)
                    viewUserMenu(user)
                }
                "A" -> {
                    MyLogger.writeInLog("Show transactions by user account")
                    viewUserTransactionsByAccount(user)
                    viewUserMenu(user)
                }
                "T" -> {
                    MyLogger.writeInLog("Show transactions by transaction type")
                    viewUserTransactionsByType(user)
                    viewUserMenu(user)
                }
                "S" -> {
                    MyLogger.writeInLog("Show transactions by transaction amount")
                    viewUserTransactionsByAmount(user)
                    viewUserMenu(user)
                }
                "M" -> {
                    MyLogger.writeInLog("Return to customer menu")
                    viewUserMenu(user)
                }
                else -> {
                    MyLogger.writeInLog("Unknown command in transaction menu")
                    println("Wrong option")
                    viewUserTransactionsMenu(user)
                }
            }
        }
    }

    private fun viewUserTransactionsByType(user: User) {
        try {
            transactionStorage.showAccountTransactionsByTransactionType(accountStorage.getAccountByUserId(user.userId).accountId)
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
    }

    private fun viewUserTransactionsByUserId(user: User) {
        try {
            transactionStorage.showAccountTransactionsByUserId(accountStorage.getAccountByUserId(user.userId).accountId)
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
    }

    private fun viewUserTransactionsByAmount(user: User) {
        try {
            transactionStorage.showAccountTransactionsByAmount(accountStorage.getAccountByUserId(user.userId).accountId)
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
    }

    private fun viewUserTransactionsByAccount(user: User) {
        try {
            transactionStorage.showAccountTransactions(accountStorage.getAccountByUserId(user.userId).accountId)
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
    }

    private fun viewUserAccount(user: User) {
        try {
            bankSystem.showUserInformation(user, accountStorage.getAccountByUserId(user.userId))
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
        MyLogger.writeInLog("Viewed user information by customer - " + user.firstName + " " + user.lastName + "(" + user.username + ")")
    }

    private fun withdrawAccount(user: User) {
        try {
            val withdrawSum = askAboutSum(user)
            try {
                if (accountStorage.withdrawAccount(
                        accountStorage.getAccountByUserId(user.userId).accountId,
                        withdrawSum,
                        transactionStorage,
                        userStorage
                    )
                ) {
                    transactionStorage.addTransaction(
                        accountStorage.getAccountByUserId(user.userId).accountId,
                        withdrawSum,
                        accountStorage.getAccountByUserId(user.userId).accountId,
                        "Withdraw"
                    )
                } else {
                    transactionStorage.addTransaction(
                        accountStorage.getAccountByUserId(user.userId).accountId,
                        withdrawSum,
                        accountStorage.getAccountByUserId(user.userId).accountId,
                        "Fraud"
                    )
                }
            } catch (e: UserException) {
                e.printStackTrace()
            }
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
    }

    private fun depositAccount(user: User) {
        try {
            val depositSum = askAboutSum(user)
            try {
                accountStorage.depositAccount(
                    accountStorage.getAccountByUserId(user.userId).accountId,
                    depositSum,
                    transactionStorage,
                    userStorage
                )
                transactionStorage.addTransaction(
                    accountStorage.getAccountByUserId(user.userId).accountId,
                    depositSum,
                    accountStorage.getAccountByUserId(user.userId).accountId,
                    "Deposit"
                )
            } catch (e: UserException) {
                e.printStackTrace()
            }
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
    }

    private fun transferBetweenAccounts(user: User) {
        val sendSum = askAboutSum(user)
        var recipientAccountId = 0
        try {
            println("Insert account Id of recipient")
            recipientAccountId = accountStorage.getAccountByAccountId(askAboutId(user)).accountId
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
        try {
            if (accountStorage.transferBetweenAccounts(
                    accountStorage.getAccountByUserId(user.userId).accountId,
                    sendSum,
                    recipientAccountId,
                    transactionStorage,
                    userStorage
                )
            ) {
                transactionStorage.addTransaction(
                    accountStorage.getAccountByUserId(user.userId).accountId,
                    sendSum,
                    recipientAccountId,
                    "Transfer"
                )
            } else {
                transactionStorage.addTransaction(
                    accountStorage.getAccountByUserId(user.userId).accountId,
                    sendSum,
                    recipientAccountId,
                    "Fraud"
                )
            }
        } catch (e: AccoutException) {
            e.printStackTrace()
        } catch (e: UserException) {
            e.printStackTrace()
        }
    }

    private fun askAboutId(user: User): Int {
        var id = 0
        if (scanner.hasNextInt()) {
            id = scanner.next().toInt()
        } else {
            MyLogger.writeInLog("Wrong input - " + scanner.next())
            viewUserMenu(user)
        }
        return id
    }

    private fun askAboutSum(user: User): Double {
        println("Write you sum - ")
        var sum = 0.0
        if (scanner.hasNextDouble()) {
            sum = scanner.next().toDouble()
        } else {
            MyLogger.writeInLog("Wrong input - " + scanner.next())
            viewUserMenu(user)
        }
        return sum
    }
}