package Logger

import java.io.IOException
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

object MyLogger {
    var log: Logger? = null
    fun createLogFile(className: String?) {
        log = Logger.getLogger(className)
        try {
            val fh: FileHandler
            fh = FileHandler("mainLogFile.log", 1000000, 5, true)
            // This block configure the logger with handler and formatter
            val formatter = SimpleFormatter()
            fh.formatter = formatter
            // the following statement is used to log any messages
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun writeInLog(message: String?) {
        log!!.info(message)
    }
}