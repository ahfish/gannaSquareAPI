package gannaSquareAPI.domain.gannSquare

import gannaSquareAPI.Common.getLogger
import gannaSquareAPI.domain.gannSquare.Direction.*
import org.springframework.stereotype.Service

interface QannSquareService {

}

typealias gannCellFunctionType = (index: Int, value : Int) -> Unit

@Service
class QannSquareServiceImpl : QannSquareService {

    val gannCellMap: MutableMap<Int, GannCell>
    val gannCells: MutableList<GannCell>
    val levelCache: MutableMap<Int, MutableList<GannCell>>

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
        gannCellMap = mutableMapOf()
        gannCells = mutableListOf()
        levelCache= mutableMapOf()
        val firstGann = GannCell(1,0 )
        gannCellMap[1] = firstGann
        (1..1000).forEach {
            val level = it
            val levelGannCell = levelCache.putIfAbsent(level,mutableListOf())
            val start = startNumberOf(level)
            val end = endNumberOf(level)
            val levelBaseRange = (start..end)
            val special = selectSpecial(levelBaseRange.toList(), level)
            val normal = selectNormal(levelBaseRange.toList(), level, special)
            if ( special.size == 8 && normal.size == 4) {

                val rightCrossCell = RightCrossCell(special[0], level)
                gannCellMap[special[0]] = rightCrossCell
                levelGannCell?.add(rightCrossCell)
                val upLeftDiagonalCell= UpLeftDiagonalCell(special[1], level)
                gannCellMap[special[1]] = upLeftDiagonalCell
                levelGannCell?.add(upLeftDiagonalCell)
                val upCrossCell = UpCrossCell(special[2], level)
                gannCellMap[special[2]] = upCrossCell
                levelGannCell?.add(upCrossCell)
                val upRightDiagonalCell = UpRightDiagonalCell(special[3], level)
                gannCellMap[special[3]] = upRightDiagonalCell
                levelGannCell?.add(upRightDiagonalCell)
                val leftCrossCell = LeftCrossCell(special[4], level)
                gannCellMap[special[4]] = leftCrossCell
                levelGannCell?.add(leftCrossCell)
                val downRightDiagonalCell = DownRightDiagonalCell(special[5], level)
                gannCellMap[special[5]] = downRightDiagonalCell
                levelGannCell?.add(downRightDiagonalCell)
                val downCrossCell = DownCrossCell(special[6], level)
                gannCellMap[special[6]] = downCrossCell
                levelGannCell?.add(downCrossCell)
                val downLeftDiagonalCell = DownLeftDiagonalCell(special[7], level)
                gannCellMap[special[7]] = downLeftDiagonalCell
                levelGannCell?.add(downLeftDiagonalCell)


                levelCache[level-1]?.filter { it is DownCrossCell }?.first().apply { (it as DownCrossCell).upThirdGannCell = rightCrossCell }
                rightCrossCell.upThirdGannCell = upCrossCell
                upCrossCell.upThirdGannCell = leftCrossCell
                leftCrossCell.upThirdGannCell = downCrossCell

                levelCache[level-1]?.filter { it is LeftCrossCell }?.first().apply { (it as LeftCrossCell).downThirdGannCell = downCrossCell }
                upCrossCell.downThirdGannCell = leftCrossCell
                rightCrossCell.downThirdGannCell = upCrossCell
                downCrossCell.downThirdGannCell = rightCrossCell

                levelCache[level-1]?.filter { it is DownLeftDiagonalCell }?.first().apply { (it as DownLeftDiagonalCell).upThirdGannCell = upLeftDiagonalCell }
                upLeftDiagonalCell.upThirdGannCell = upRightDiagonalCell
                upRightDiagonalCell.upThirdGannCell = downRightDiagonalCell
                downRightDiagonalCell.upThirdGannCell = downLeftDiagonalCell

                levelCache[level-1]?.filter { it is UpLeftDiagonalCell }?.first().apply { (it as UpLeftDiagonalCell).downThirdGannCell = downLeftDiagonalCell }
                downLeftDiagonalCell.downThirdGannCell = downRightDiagonalCell
                downRightDiagonalCell.downThirdGannCell = upRightDiagonalCell
                upRightDiagonalCell.downThirdGannCell = upLeftDiagonalCell

                val assignNormalCell : (firstDiagonalCell : DiagonalCell, crossCell : CrossCell, secondDiagonalCell : DiagonalCell, direction : Direction, numbers : List<Int>  ) -> gannCellFunctionType =
                        { firstDiagonalCell,crossCell,secondDiagonalCell, direction, numbers ->
                            { index: Int, base : Int ->
                                if ((level == 4 && index == 0) || (level > 4 && index in 0..1)) {
                                    gannCellMap[base] = NormalCell(base, level, direction, firstDiagonalCell)
                                } else if ((level == 4 && base == numbers.last()) || (level > 4 && base in numbers.takeLast(2))) {
                                    gannCellMap[base] = NormalCell(base, level, direction, secondDiagonalCell)
                                } else {
                                    gannCellMap[base] = NormalCell(base, level, direction, crossCell)
                                }
                                levelGannCell?.add(gannCellMap[base]!!)
                        }
                }

                normal[0].forEachIndexed(assignNormalCell(downLeftDiagonalCell,rightCrossCell, upLeftDiagonalCell, LEFT, normal[0]))
                normal[1].forEachIndexed(assignNormalCell(upLeftDiagonalCell,upCrossCell, upRightDiagonalCell, UP, normal[1]))
                normal[2].forEachIndexed(assignNormalCell(upRightDiagonalCell,leftCrossCell, downRightDiagonalCell, RIGHT, normal[2]))
                normal[3].forEachIndexed(assignNormalCell(downRightDiagonalCell,downCrossCell, downLeftDiagonalCell, DOWN,normal[3]))
            } else {
                log.error("Level ${level} contain not enough special gann cell [${special.size}] or nominal size is not enough [${normal.size}]")
            }
        }

        gannCellMap.keys.sorted().forEach {
            gannCells.add(gannCellMap[it]!!)
        }

//        gannCells.filter { it.level == 6 }.forEach {
//            log.info(it.toString())
//        }


    }

    fun gannSquareResultOf(base : Int) : GannSquareResult? {
//        gannCells.filter { it.base > }
        return null;
    }


}

