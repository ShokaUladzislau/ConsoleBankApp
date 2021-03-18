package Storages

import Exceptions.AccoutException
import Exceptions.UserException
import Logger.MyLogger
import Models.User
import java.io.*
import java.util.*

class UserStorage(var accountStorage: AccountStorage) {
    var users: MutableList<User> = ArrayList()
    fun addUser(user: User?, userType: String) {
        MyLogger.writeInLog("Add user to users list")
        if (user == null) {
            return
        }
        for (userInList in users) {
            if (user.username == userInList.username) {
                MyLogger.writeInLog("You can't use this username.")
                return
            }
        }
        user.userId = users.size
        user.userRole = userType
        users.add(user)
        if (userType != "Employee") {
            accountStorage.addAccount(user)
        }
        MyLogger.writeInLog("User added to user list")
    }

    fun serializeUsers() {
        try {
            val fileOutputStream = FileOutputStream("users-file.json")
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(users)
            fileOutputStream.close()
            objectOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deserializeUsers() {
        try {
            val fileInputStream = FileInputStream("users-file.json")
            val objectInputStream = ObjectInputStream(fileInputStream)
            users = objectInputStream.readObject() as ArrayList<User>
            fileInputStream.close()
            objectInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun removeUser(user: User?) {
        MyLogger.writeInLog("Remove user form users list")
        if (user == null) {
            return
        }
        try {
            accountStorage.deleteAccountByUserId(user.userId)
            users.remove(user)
            MyLogger.writeInLog("User has been deleted.")
            MyLogger.writeInLog("User deleted from user list")
        } catch (e: AccoutException) {
            e.printStackTrace()
        }
    }

    @Throws(UserException::class)
    fun getUserByUsernameAndPass(userName: String, password: String): User {
        MyLogger.writeInLog("Getting user by username and password from users list")
        for (userInList in users) {
            if (userName == userInList.username && password == userInList.password) {
                MyLogger.writeInLog("Success")
                return userInList
            }
        }
        MyLogger.writeInLog("Not found this login and pass combination")
        throw UserException("Not found this login and pass")
    }

    @Throws(UserException::class)
    fun getUserByUsername(userName: String): User {
        MyLogger.writeInLog("Getting user by username from users list")
        for (userInList in users) {
            if (userName == userInList.username) {
                MyLogger.writeInLog("Success")
                return userInList
            }
        }
        MyLogger.writeInLog("Not found this login")
        throw UserException("Not found this login")
    }

    @Throws(UserException::class)
    fun getUserByUserId(userId: Int): User {
        MyLogger.writeInLog("Getting user by userId from users list")
        for (userInList in users) {
            if (userId == userInList.userId) {
                MyLogger.writeInLog("Success")
                return userInList
            }
        }
        MyLogger.writeInLog("Not found this user")
        throw UserException("Not found this user")
    }

    @Throws(UserException::class)
    fun getUserByAccountId(accountId: Int): User {
        MyLogger.writeInLog("Getting user by userId from users list")
        for (userInList in users) {
            if (accountId == userInList.userId) {
                MyLogger.writeInLog("Success")
                return userInList
            }
        }
        MyLogger.writeInLog("Not found this user")
        throw UserException("Not found this user")
    }
}