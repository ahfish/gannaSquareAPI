package gannaSquareAPI.domain.gannSquare

import gannaSquareAPI.Common.getLogger
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

interface QannSquareService {

}

@Service
class QannSquareServiceImpl : QannSquareService {

    val gannCellMap: MutableMap<Int, GannCell>
    val gannCells: MutableList<GannCell>
    //val levelCache: MutableMap<Int, MutableMap<String, MutableList<GannCell>>>

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass.enclosingClass)
    }

    fun startNumberOf(level: Int): Int {
        return (1..level).fold(1) { sum, level -> ((level - 1) * 8) + sum } + 1
    }

    fun endNumberOf(level: Int): Int =
            (1..level).fold(1) { sum, level -> ((level) * 8) + sum }


    fun selectSpecial(numbers: List<Int>, level: Int): List<Int> =
            numbers.chunked(level) { numberGroup -> numberGroup.last() }


    fun selectNormal(numbers: List<Int>, level: Int, exclude: List<Int>): List<List<Int>> =
            numbers.chunked(level * 2) { numbers ->
                numbers.minus(exclude)
            }

    init {
        val start = startNumberOf(3)
        val end = endNumberOf(3)
        val special = selectSpecial((start..end).toList(), 3)
        val nominal = selectNormal((start..end).toList(), 3, special)
        log.info("Start number of level 1 ${startNumberOf(1)}, end ${endNumberOf(1)}")
        log.info("Start number of level 1 ${startNumberOf(2)}, end ${endNumberOf(2)}")
        log.info("Start number of level 1 ${startNumberOf(3)}, end ${endNumberOf(3)}")

        log.info("level 3 special ${special}")
        log.info("level 3 nominal ${nominal}")
        gannCellMap = mutableMapOf()
        gannCells = mutableListOf()
        val firstGann = GannCell(1,0 )
        gannCellMap[1] = firstGann
        (1..1000).forEach {
            val level = it
            val start = startNumberOf(level)
            val end = endNumberOf(level)
            val levelBaseRange = (start..end)
            val special = selectSpecial(levelBaseRange.toList(), level)
            val nominal = selectNormal(levelBaseRange.toList(), level, special)
            if ( special.size == 8 && nominal.size == 4) {
                gannCellMap[special[0]] = RightCrossCell(special[0], level)
                gannCellMap[special[1]] = UpLeftDiagonalCell(special[1], level)
                gannCellMap[special[2]] = UpCrossCell(special[2], level)
                gannCellMap[special[3]] = UpRightDiagonalCell(special[3], level)
                gannCellMap[special[4]] = LeftCrossCell(special[4], level)
                gannCellMap[special[5]] = DownRightDiagonalCell(special[5], level)
                gannCellMap[special[6]] = DownCrossCell(special[6], level)
                gannCellMap[special[7]] = DownLeftDiagonalCell(special[7], level)
                nominal[0].forEach {
                    gannCellMap[it] = RightCell(it, level)
                }
                nominal[1].forEach {
                    gannCellMap[it] = UpCell(it, level)
                }
                nominal[2].forEach {
                    gannCellMap[it] = LeftCell(it, level)
                }
                nominal[3].forEach {
                    gannCellMap[it] = DownCell(it, level)
                }
            } else {
                log.error("Level ${level} contain not enough special gann cell [${special.size}] or nominal size is not enough [${nominal.size}]")
            }
        }

        gannCellMap.keys.sorted().forEach {
            gannCells.add(gannCellMap[it]!!)
        }


    }

    fun gannSquareResultOf(base : Int) : GannSquareResult? {
//        gannCells.filter { it.base > }
        return null;
    }


}
