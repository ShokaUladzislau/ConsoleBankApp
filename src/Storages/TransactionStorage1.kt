package Storages

import Exceptions.AccoutException
import Logger.MyLogger
import Models.Transaction
import java.io.*
import java.time.LocalDateTime
import java.util.*
import java.util.function.Function

class TransactionStorage(var accountStorage: AccountStorage) {
    var transactions: MutableList<Transaction> = ArrayList()
    @Throws(AccoutException::class)
    fun addTransaction(senderAccountId: Int, amount: Double, recipientAccountId: Int, transactionType: String?) {
        val transaction = Transaction()
        if (amount > 0) {
            transaction.transactionId = transactions.size
            transaction.senderId = accountStorage.getAccountByAccountId(senderAccountId).accountId
            transaction.recipientId = accountStorage.getAccountByAccountId(recipientAccountId).accountId
            transaction.amount = amount
            transaction.typeOfTransaction = transactionType
            transaction.dateAndTimeOfTransaction = LocalDateTime.now()
            transactions.add(transaction)
            MyLogger.writeInLog("Transaction added to transaction list")
        } else {
            MyLogger.writeInLog("incorrect sum of transaction")
        }
    }

    fun showAccountTransactions(accountId: Int) {
        val accountTransactions: MutableList<Transaction> = ArrayList()
        if (transactions.size == 0) {
            MyLogger.writeInLog("You dont have any transaction yet.")
            return
        }
        for (transactionInList in transactions) {
            if (accountId == transactionInList.senderId || accountId == transactionInList.recipientId) {
                accountTransactions.add(transactionInList)
                MyLogger.writeInLog("Get transaction by account Id")
            }
        }
        for (transactionInList in accountTransactions) {
            println(
                "| Transaction ID - " + transactionInList.transactionId + " " +
                        "| Sender ID - " + transactionInList.senderId + " " +
                        "| Recipient ID - " + transactionInList.recipientId + " " +
                        "| Transaction amount - " + transactionInList.amount + " " +
                        "| Transaction type - " + transactionInList.typeOfTransaction + " " +
                        "| Transaction date - " + transactionInList.dateAndTimeOfTransaction
            )
        }
    }

    fun showAccountTransactionsByAmount(accountId: Int) {
        val accountTransactions: MutableList<Transaction> = ArrayList()
        if (transactions.size == 0) {
            MyLogger.writeInLog("You dont have any transaction yet.")
            return
        }
        for (transactionInList in transactions) {
            if (accountId == transactionInList.senderId || accountId == transactionInList.recipientId) {
                accountTransactions.add(transactionInList)
                MyLogger.writeInLog("Get transaction by account Id")
            }
        }
        accountTransactions.sortWith(Comparator { t1: Transaction, t2: Transaction -> ((t1.amount - t2.amount)).toInt() })
        for (transactionInList in accountTransactions) {
            println(
                "| Transaction ID - " + transactionInList.transactionId + " " +
                        "| Sender ID - " + transactionInList.senderId + " " +
                        "| Recipient ID - " + transactionInList.recipientId + " " +
                        "| Transaction amount - " + transactionInList.amount + " " +
                        "| Transaction type - " + transactionInList.typeOfTransaction + " " +
                        "| Transaction date - " + transactionInList.dateAndTimeOfTransaction
            )
        }
    }

    fun showAllTransactions() {
        val accountTransactions: MutableList<Transaction> = ArrayList()
        if (transactions.size == 0) {
            MyLogger.writeInLog("You dont have any transaction yet.")
            return
        }
        for (transactionInList in transactions) {
            accountTransactions.add(transactionInList)
            MyLogger.writeInLog("Get transaction by account Id")
        }
        for (transactionInList in accountTransactions) {
            println(
                "| Transaction ID - " + transactionInList.transactionId + " " +
                        "| Sender ID - " + transactionInList.senderId + " " +
                        "| Recipient ID - " + transactionInList.recipientId + " " +
                        "| Transaction amount - " + transactionInList.amount + " " +
                        "| Transaction type - " + transactionInList.typeOfTransaction + " " +
                        "| Transaction date - " + transactionInList.dateAndTimeOfTransaction
            )
        }
    }

    fun showAccountTransactionsByUserId(accountId: Int) {
        val accountTransactions: MutableList<Transaction> = ArrayList()
        if (transactions.size == 0) {
            MyLogger.writeInLog("You dont have any transaction yet.")
            return
        }
        for (transactionInList in transactions) {
            if (accountId == transactionInList.senderId || accountId == transactionInList.recipientId) {
                accountTransactions.add(transactionInList)
                MyLogger.writeInLog("Get transaction by account Id")
            }
        }
        accountTransactions.sortWith(Comparator { t1: Transaction, t2: Transaction -> (t1.senderId - t2.senderId) })
        for (transactionInList in accountTransactions) {
            println(
                "| Transaction ID - " + transactionInList.transactionId + " " +
                        "| Sender ID - " + transactionInList.senderId + " " +
                        "| Recipient ID - " + transactionInList.recipientId + " " +
                        "| Transaction amount - " + transactionInList.amount + " " +
                        "| Transaction type - " + transactionInList.typeOfTransaction + " " +
                        "| Transaction date - " + transactionInList.dateAndTimeOfTransaction
            )
        }
    }

    fun showAccountTransactionsByTransactionType(accountId: Int) {
        val accountTransactions: MutableList<Transaction> = ArrayList()
        if (transactions.size == 0) {
            MyLogger.writeInLog("You dont have any transaction yet.")
            return
        }
        for (transactionInList in transactions) {
            if (accountId == transactionInList.senderId || accountId == transactionInList.recipientId) {
                accountTransactions.add(transactionInList)
                MyLogger.writeInLog("Get transaction by account Id")
            }
        }
        accountTransactions.sortWith(Comparator { t1: Transaction, t2: Transaction -> (t1.typeOfTransaction.toString().compareTo(t2.typeOfTransaction.toString())) })
        for (transactionInList in accountTransactions) {
            println(
                "| Transaction ID - " + transactionInList.transactionId + " " +
                        "| Sender ID - " + transactionInList.senderId + " " +
                        "| Recipient ID - " + transactionInList.recipientId + " " +
                        "| Transaction amount - " + transactionInList.amount + " " +
                        "| Transaction type - " + transactionInList.typeOfTransaction + " " +
                        "| Transaction date - " + transactionInList.dateAndTimeOfTransaction
            )
        }
    }

    fun serializeTransactions() {
        try {
            val fileOutputStream = FileOutputStream("transactions-file.json")
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(transactions)
            fileOutputStream.close()
            objectOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deserializeTransactions() {
        try {
            val fileInputStream = FileInputStream("transactions-file.json")
            val objectInputStream = ObjectInputStream(fileInputStream)
            transactions = objectInputStream.readObject() as ArrayList<Transaction>
            fileInputStream.close()
            objectInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
}