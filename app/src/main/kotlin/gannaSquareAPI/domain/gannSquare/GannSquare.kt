package gannaSquareAPI.domain.gannSquare

import gannaSquareAPI.Common.getLogger
import org.springframework.stereotype.Service

interface QannSquareService {

}

@Service
class QannSquareServiceImpl : QannSquareService {

    val gannCell : MutableMap<Int, GannCell>
    val levelCache : MutableMap<Int, MutableMap<String, MutableList<GannCell>>>

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass.enclosingClass)
    }

    init {
        gannCell = mutableMapOf()
        levelCache = mutableMapOf()
        log.info("initialize QannSquareService")
        var level = 0
        val firstGann = GannCell(1,level++ )

        (2..500).forEach {base ->
            val edgeMax = level * 2
            val levelGannCell = levelCache.putIfAbsent(level, mutableMapOf())
//            if ( )

        }
    }

}