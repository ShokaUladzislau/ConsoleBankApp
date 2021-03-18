package Revature

import BusinessLogic.BankSystem
import Exceptions.UserException
import Logger.MyLogger

object Main {
    @Throws(UserException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        MyLogger.createLogFile(Main::class.java.name)
        MyLogger.writeInLog("Banking system launch")
        val bankSystem: BankSystem = BankSystem.instance
        bankSystem.showMenu()
    }
}