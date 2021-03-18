package Storages

import Exceptions.AccoutException
import Exceptions.UserException
import Logger.MyLogger
import Models.Account
import Models.User
import java.io.*
import java.util.*

class AccountStorage {
    var accounts: MutableList<Account> = ArrayList()
    fun addAccount(user: User) {
        MyLogger.writeInLog("Add an account to new user")
        val account = Account()
        account.accountId = user.userId
        account.balance = 0.0
        account.accountStatus = "Unapproved"
        accounts.add(account)
        MyLogger.writeInLog("Account has been created")
    }

    @Throws(AccoutException::class)
    fun getAccountByUserId(userId: Int): Account {
        MyLogger.writeInLog("Getting account by user id")
        for (accountsInList in accounts) {
            if (userId == accountsInList.accountId) {
                MyLogger.writeInLog("Success")
                return accountsInList
            }
        }
        MyLogger.writeInLog("Not found this account")
        throw AccoutException("Not found this account")
    }

    @Throws(AccoutException::class)
    fun getAccountByAccountId(accountId: Int): Account {
        MyLogger.writeInLog("Getting account by account id")
        for (accountsInList in accounts) {
            if (accountId == accountsInList.accountId) {
                MyLogger.writeInLog("Success")
                return accountsInList
            }
        }
        MyLogger.writeInLog("Not found this account")
        throw AccoutException("Not found this account")
    }

    @Throws(AccoutException::class, UserException::class)
    fun depositAccount(
        accountId: Int,
        depositSum: Double,
        transactionStorage: TransactionStorage?,
        userStorage: UserStorage
    ) {
        MyLogger.writeInLog("Depositing account by user id")
        for (accountsInList in accounts) {
            if (accountId == accountsInList.accountId && accountsInList.accountStatus == "Approved") {
                if (depositSum > 0) {
                    val user = userStorage.getUserByAccountId(accountId)
                    accountsInList.balance = accountsInList.balance + depositSum
                    MyLogger.writeInLog("Success")
                    MyLogger.writeInLog("Deposited customer account by customer - " + user.firstName + " " + user.lastName + "(" + user.username + ")")
                } else {
                    MyLogger.writeInLog("Incorrect sum")
                    return
                }
                return
            } else if (accountsInList.accountStatus == "Blocked") {
                MyLogger.writeInLog("Your account is blocked")
            } else if (accountsInList.accountStatus == "Unapproved") {
                MyLogger.writeInLog("Your account is unapproved")
            }
        }
        MyLogger.writeInLog("Not get this account")
        throw AccoutException("Not get this account")
    }

    @Throws(AccoutException::class, UserException::class)
    fun withdrawAccount(
        accountId: Int,
        withdrawSum: Double,
        transactionStorage: TransactionStorage?,
        userStorage: UserStorage
    ): Boolean {
        MyLogger.writeInLog("Withdrawing account by user id")
        for (accountsInList in accounts) {
            if (accountId == accountsInList.accountId && accountsInList.accountStatus == "Approved") {
                return if (withdrawSum > accountsInList.balance || withdrawSum <= 0) {
                    MyLogger.writeInLog("Incorrect sum")
                    false
                } else {
                    val user = userStorage.getUserByAccountId(accountId)
                    accountsInList.balance = accountsInList.balance - withdrawSum
                    MyLogger.writeInLog("Success")
                    MyLogger.writeInLog("Withdrawn customer account by customer - " + user.firstName + " " + user.lastName + "(" + user.username + ")")
                    true
                }
            } else if (accountsInList.accountStatus == "Blocked") {
                MyLogger.writeInLog("Your account is blocked")
            } else if (accountsInList.accountStatus == "Unapproved") {
                MyLogger.writeInLog("Your account is unapproved")
            }
        }
        MyLogger.writeInLog("Not get this account")
        throw AccoutException("Not get this account")
    }

    @Throws(AccoutException::class, UserException::class)
    fun transferBetweenAccounts(
        senderAccountId: Int,
        sendSum: Double,
        recipientAccountId: Int,
        transactionStorage: TransactionStorage?,
        userStorage: UserStorage
    ): Boolean {
        return if (withdrawAccount(senderAccountId, sendSum.toDouble(), transactionStorage, userStorage)) {
            depositAccount(recipientAccountId, sendSum.toDouble(), transactionStorage, userStorage)
            true
        } else {
            false
        }
    }

    @Throws(AccoutException::class)
    fun approveUserAccount(userId: Int) {
        val account = getAccountByUserId(userId)
        MyLogger.writeInLog("Approved account - $userId")
        account.accountStatus = "Approved"
    }

    @Throws(AccoutException::class)
    fun deleteAccountByUserId(userId: Int) {
        val account = getAccountByUserId(userId)
        accounts.remove(account)
        MyLogger.writeInLog("Account has been deleted")
    }

    @Throws(AccoutException::class)
    fun blockUserAccount(userId: Int) {
        val account = getAccountByUserId(userId)
        MyLogger.writeInLog("Blocked account - $userId")
        account.accountStatus = "Blocked"
    }

    fun deserializeAccounts() {
        try {
            val fileInputStream = FileInputStream("accounts-file.json")
            val objectInputStream = ObjectInputStream(fileInputStream)
            accounts = objectInputStream.readObject() as ArrayList<Account>
            fileInputStream.close()
            objectInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun serializeAccounts() {
        try {
            val fileOutputStream = FileOutputStream("accounts-file.json")
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(accounts)
            fileOutputStream.close()
            objectOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}