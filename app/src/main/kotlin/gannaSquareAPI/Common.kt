package gannaSquareAPI

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Common {
    fun getLogger(forClass: Class<*>): Logger =
            LoggerFactory.getLogger(forClass)

}