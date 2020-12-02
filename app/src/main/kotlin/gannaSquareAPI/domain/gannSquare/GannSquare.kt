package gannaSquareAPI.domain.gannSquare

import gannaSquareAPI.Common.getLogger
import gannaSquareAPI.domain.gannSquare.Direction.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

interface QannSquareService {
    fun gannSquareResultOf(base: Int): GannSquareResult
    fun gannSquareResultOf(base: BigDecimal, digit: BigDecimal): GannSquareResult
}

typealias gannCellFunctionType = (index: Int, value: Int) -> Unit

@Service
@ExperimentalTime
class QannSquareServiceImpl : QannSquareService {

    val gannCellMap: MutableMap<Int, GannCell>
    val specialGannCellsAsc: MutableList<GannSpecialCell>
    val specialGannCellsDesc: MutableList<GannSpecialCell>
    val levelCache: MutableMap<Int, MutableList<GannCell>>
    val maxBase : Int

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
        log.info("initialize QannSquareServiceImpl")
        val time = measureTime {
            gannCellMap = mutableMapOf()
            levelCache = mutableMapOf()
            specialGannCellsAsc = mutableListOf()
            specialGannCellsDesc = mutableListOf()
            val firstGann = GannCell(1, 0, 0)
            gannCellMap[1] = firstGann
            (1..500).forEach {
                val level = it
                val levelGannCell = mutableListOf<GannCell>()

                val start = startNumberOf(level)
                val end = endNumberOf(level)
                val levelBaseRange = (start..end)
                val special = selectSpecial(levelBaseRange.toList(), level)
                val normal = selectNormal(levelBaseRange.toList(), level, special)
                if (special.size == 8 && normal.size == 4) {

                    val leftCrossCell = LeftCrossCell(special[0], level)
                    gannCellMap[special[0]] = leftCrossCell
                    levelGannCell?.add(leftCrossCell)

                    val upLeftDiagonalCell = UpLeftDiagonalCell(special[1], level)
                    gannCellMap[special[1]] = upLeftDiagonalCell
                    levelGannCell?.add(upLeftDiagonalCell)

                    val upCrossCell = UpCrossCell(special[2], level)
                    gannCellMap[special[2]] = upCrossCell
                    levelGannCell?.add(upCrossCell)

                    val upRightDiagonalCell = UpRightDiagonalCell(special[3], level)
                    gannCellMap[special[3]] = upRightDiagonalCell
                    levelGannCell?.add(upRightDiagonalCell)

                    val rightCrossCell = RightCrossCell(special[4], level)
                    gannCellMap[special[4]] = rightCrossCell
                    levelGannCell?.add(rightCrossCell)

                    val downRightDiagonalCell = DownRightDiagonalCell(special[5], level)
                    gannCellMap[special[5]] = downRightDiagonalCell
                    levelGannCell?.add(downRightDiagonalCell)

                    val downCrossCell = DownCrossCell(special[6], level)
                    gannCellMap[special[6]] = downCrossCell
                    levelGannCell?.add(downCrossCell)

                    val downLeftDiagonalCell = DownLeftDiagonalCell(special[7], level)
                    gannCellMap[special[7]] = downLeftDiagonalCell
                    levelGannCell?.add(downLeftDiagonalCell)

                    leftCrossCell.upThirdGannCell = upCrossCell
                    rightCrossCell.upThirdGannCell = downCrossCell
                    upCrossCell.upThirdGannCell = rightCrossCell
                    levelCache[level - 1]?.filterIsInstance<DownCrossCell>()?.firstOrNull().let {
                        it?.upThirdGannCell = leftCrossCell
                    }


                    levelCache[level - 1]?.filterIsInstance<DownCrossCell>()?.firstOrNull().let {
                        leftCrossCell.downThirdGannCell = it
                    }
                    upCrossCell.downThirdGannCell = leftCrossCell
                    rightCrossCell.downThirdGannCell = upCrossCell
                    downCrossCell.downThirdGannCell = rightCrossCell

                    levelCache[level - 1]?.filterIsInstance<DownLeftDiagonalCell>()?.firstOrNull().let {
                        it?.upThirdGannCell = upLeftDiagonalCell
                    }
                    upLeftDiagonalCell.upThirdGannCell = upRightDiagonalCell
                    upRightDiagonalCell.upThirdGannCell = downRightDiagonalCell
                    downRightDiagonalCell.upThirdGannCell = downLeftDiagonalCell

                    levelCache[level - 1]?.filterIsInstance<DownLeftDiagonalCell>()?.firstOrNull().let {
                        upLeftDiagonalCell.downThirdGannCell = it
                    }
                    downLeftDiagonalCell.downThirdGannCell = downRightDiagonalCell
                    downRightDiagonalCell.downThirdGannCell = upRightDiagonalCell
                    upRightDiagonalCell.downThirdGannCell = upLeftDiagonalCell

                    val assignNormalCell: (firstDiagonalCell: DiagonalCell, crossCell: CrossCell, secondDiagonalCell: DiagonalCell, direction: Direction, numbers: List<Int>) -> gannCellFunctionType =
                            { firstDiagonalCell, crossCell, secondDiagonalCell, direction, numbers ->
                                { index: Int, base: Int ->
                                    if ((level == 4 && index == 0) || (level > 4 && index in 0..1)) {
                                        if ( firstDiagonalCell is DownLeftDiagonalCell) {
                                            gannCellMap[base] = NormalCell(base, level, direction, firstDiagonalCell)
                                        } else {
                                            gannCellMap[base] = NormalCell(base, level, direction, index+1, firstDiagonalCell)
                                        }
                                    } else if ((level == 4 && base == numbers.last()) || (level > 4 && base in numbers.takeLast(2))) {
                                        gannCellMap[base] = NormalCell(base, level, direction, secondDiagonalCell)
                                    } else {
                                        gannCellMap[base] = NormalCell(base, level, direction, crossCell)
                                    }
                                    levelGannCell?.add(gannCellMap[base]!!)
                                }
                            }

                    normal[0].forEachIndexed(assignNormalCell(downLeftDiagonalCell, leftCrossCell, upLeftDiagonalCell, LEFT, normal[0]))
                    normal[1].forEachIndexed(assignNormalCell(upLeftDiagonalCell, upCrossCell, upRightDiagonalCell, UP, normal[1]))
                    normal[2].forEachIndexed(assignNormalCell(upRightDiagonalCell, rightCrossCell, downRightDiagonalCell, RIGHT, normal[2]))
                    normal[3].forEachIndexed(assignNormalCell(downRightDiagonalCell, downCrossCell, downLeftDiagonalCell, DOWN, normal[3]))
                    levelGannCell.sortBy { it.base }
                    levelCache.putIfAbsent(level, levelGannCell)
                } else {
                    log.error("Level ${level} contain not enough special gann cell [${special.size}] or nominal size is not enough [${normal.size}]")
                }
            }

            gannCellMap.keys.sorted().forEach {
                if ( gannCellMap[it] is GannSpecialCell) specialGannCellsAsc.add(gannCellMap[it] as GannSpecialCell)
            }

            gannCellMap.keys.reversed().forEach {
                if ( gannCellMap[it] is GannSpecialCell) specialGannCellsDesc.add(gannCellMap[it] as GannSpecialCell)
            }
//        log.info("down 130 - ${downTrendSpecialGannCell(gannCellMap[130]!!)?.base}")
//        log.info("up 130 - ${upTrendSpecialGannCell(gannCellMap[130]!!)?.base}")
//
//        log.info("down 132 - ${downTrendSpecialGannCell(gannCellMap[132]!!)?.base}")
//        log.info("up 132 - ${upTrendSpecialGannCell(gannCellMap[132]!!)?.base}")
//
//        log.info("down 224 - ${downTrendSpecialGannCell(gannCellMap[224]!!)?.base}")
//        log.info("up 224 - ${upTrendSpecialGannCell(gannCellMap[224]!!)?.base}")
//
//        log.info("down 133 - ${downTrendSpecialGannCell(gannCellMap[133]!!)?.base}")
//        log.info("up 133 - ${upTrendSpecialGannCell(gannCellMap[133]!!)?.base}")
//
//        log.info("down 134 - ${downTrendSpecialGannCell(gannCellMap[134]!!)?.base}")
//        log.info("up 134 - ${upTrendSpecialGannCell(gannCellMap[134]!!)?.base}")
//            var id = 40
//        log.info("secondLevelBaseGannSquareOf ${id} - ${gannCellMap[id]!!.secondLevelGannCell!!.base}")
//        id = 222
//        log.info("secondLevelBaseGannSquareOf ${id} - ${gannCellMap[id]!!.secondLevelGannCell!!.base}")
//        id = 223
//        log.info("secondLevelBaseGannSquareOf ${id} - ${gannCellMap[id]!!.secondLevelGannCell!!.base}")
//
//        id = 213
//        log.info("secondLevelBaseGannSquareOf ${id} - ${gannCellMap[id]!!.secondLevelGannCell!!.base}")
//
//        id = 272
//        log.info("secondLevelBaseGannSquareOf ${id} - ${gannCellMap[id]!!.secondLevelGannCell!!.base}")
//        id = 162
//        log.info("secondLevelBaseGannSquareOf ${id} - ${gannCellMap[id]!!.secondLevelGannCell!!.base}")
//        id = 168
//        log.info("secondLevelBaseGannSquareOf ${id} - ${gannCellMap[id]!!.secondLevelGannCell!!.base}")

//        id = 134
//        log.info("secondLevelBaseGannSquareOf ${id} - ${secondLevelBaseGannSquareOf(gannCellMap[id]!!)?.base}")
//        id = 131
//        log.info("secondLevelBaseGannSquareOf ${id} - ${secondLevelBaseGannSquareOf(gannCellMap[id]!!)?.base}")
//        id = 130
//        log.info("secondLevelBaseGannSquareOf ${id} - ${secondLevelBaseGannSquareOf(gannCellMap[id]!!)?.base}")
//
//        log.info("secondLevelBaseGannSquareOf 146 - ${secondLevelBaseGannSquareOf(gannCellMap[146]!!)?.base}")
//        log.info("secondLevelBaseGannSquareOf 323 - ${secondLevelBaseGannSquareOf(gannCellMap[323]!!)?.base}")
//        log.info("secondLevelBaseGannSquareOf 207 - ${secondLevelBaseGannSquareOf(gannCellMap[207]!!)?.base}")
//        log.info("secondLevelBaseGannSquareOf 205 - ${secondLevelBaseGannSquareOf(gannCellMap[205]!!)?.base}")

//        log.info("secondLevelGannSquareOf 134 - ${secondLevelBaseGannSquareOf(gannCellMap[134]!!).upThirdGannCell}, ${secondLevelBaseGannSquareOf(gannCellMap[134]!!).downThirdGannCell}")
//        log.info("secondLevelGannSquareOf 131 - ${secondLevelGannSquareOf(gannCellMap[131]!!)}")
//        log.info("secondLevelGannSquareOf 130 - ${secondLevelGannSquareOf(gannCellMap[130]!!)}")
//        log.info("secondLevelGannSquareOf 146 - ${secondLevelGannSquareOf(gannCellMap[146]!!)}")
//        log.info("secondLevelGannSquareOf 323 - ${secondLevelGannSquareOf(gannCellMap[323]!!)}")
//        log.info("secondLevelGannSquareOf 207 - ${secondLevelGannSquareOf(gannCellMap[207]!!)}")
//        log.info("secondLevelGannSquareOf 205 - ${secondLevelGannSquareOf(gannCellMap[205]!!)}")

//        log.info("firstLevelGannSquareOf ${id} - ${firstLevelGannSquareOf(gannCellMap[id]!!).first?.base},  ${firstLevelGannSquareOf(gannCellMap[id]!!).second?.base}")
//        id = 192
//        log.info("firstLevelGannSquareOf ${id} - ${firstLevelGannSquareOf(gannCellMap[id]!!).first?.base},  ${firstLevelGannSquareOf(gannCellMap[id]!!).second?.base}")
//        id = 179
//        log.info("firstLevelGannSquareOf ${id} - ${firstLevelGannSquareOf(gannCellMap[id]!!).first?.base},  ${firstLevelGannSquareOf(gannCellMap[id]!!).second?.base}")
//        id = 178
//        log.info("firstLevelGannSquareOf ${id} - ${firstLevelGannSquareOf(gannCellMap[id]!!).first?.base},  ${firstLevelGannSquareOf(gannCellMap[id]!!).second?.base}")
//        id = 172
//        log.info("firstLevelGannSquareOf ${id} - ${firstLevelGannSquareOf(gannCellMap[id]!!).first?.base},  ${firstLevelGannSquareOf(gannCellMap[id]!!).second?.base}")
//        id = 171
//        log.info("firstLevelGannSquareOf ${id} - ${firstLevelGannSquareOf(gannCellMap[id]!!).first?.base},  ${firstLevelGannSquareOf(gannCellMap[id]!!).second?.base}")
//        id = 284
//        log.info("firstLevelGannSquareOf ${id} - ${firstLevelGannSquareOf(gannCellMap[id]!!).first?.base},  ${firstLevelGannSquareOf(gannCellMap[id]!!).second?.base}")
//        id = 283
//        log.info("firstLevelGannSquareOf ${id} - ${firstLevelGannSquareOf(gannCellMap[id]!!).first?.base},  ${firstLevelGannSquareOf(gannCellMap[id]!!).second?.base}")
//            id = 192
//        log.info("thirdGannSqaureOf ${id} - ${thirdGannSqaureOf(gannCellMap[id]!!).first?.base},  ${thirdGannSqaureOf(gannCellMap[id]!!).second?.base}")

//            id = 195
//        log.info("thirdGannSqaureOf ${id} - ${thirdGannSqaureOf(gannCellMap[id]!!).first?.base},  ${thirdGannSqaureOf(gannCellMap[id]!!).second?.base}")

//            id = 388
//            log.info("thirdGannSqaureOf ${id} - ${thirdGannSqaureOf(gannCellMap[id]!!).first?.base},  ${thirdGannSqaureOf(gannCellMap[id]!!).second?.base}")
//            id = 389
//            log.info("thirdGannSqaureOf ${id} - ${thirdGannSqaureOf(gannCellMap[id]!!).first?.base},  ${thirdGannSqaureOf(gannCellMap[id]!!).second?.base}")
//            id = 390
//            log.info("thirdGannSqaureOf ${id} - ${thirdGannSqaureOf(gannCellMap[id]!!).first?.base},  ${thirdGannSqaureOf(gannCellMap[id]!!).second?.base}")
//            id = 317
//            log.info("thirdGannSqaureOf ${id} - ${thirdGannSqaureOf(gannCellMap[id]!!).first?.base},  ${thirdGannSqaureOf(gannCellMap[id]!!).second?.base}")
        }
        maxBase= gannCellMap.keys.sorted().last()
        log.info("Done in ${time}, last base is ${maxBase}")
    }

    override fun gannSquareResultOf(base: BigDecimal, digit: BigDecimal): GannSquareResult {
        var targetBase = BigDecimal.ZERO
        val intPart = base.setScale(0, BigDecimal.ROUND_DOWN)
        val decimalPart  = base.subtract(intPart)
        var numberOfDecimalPoint = BigDecimal.ZERO
        if ( !BigDecimal.ZERO.equals(intPart) ) {
            targetBase = intPart
        }
        if ( !BigDecimal.ZERO.equals(decimalPart) ) {
            numberOfDecimalPoint = BigDecimal(decimalPart.toPlainString().length - 2)
            targetBase = targetBase.multiply(BigDecimal.TEN.pow(numberOfDecimalPoint.toInt())).add(decimalPart.multiply(BigDecimal.TEN.pow(numberOfDecimalPoint.toInt()))).setScale(0, BigDecimal.ROUND_DOWN)
        }
        val remainingNumberOfDigit = if ( (targetBase.toPlainString().length - digit.toInt()) > 0 ) targetBase.toPlainString().length - digit.toInt() else 0
        val gannSquareResult = gannSquareResultOf(BigDecimal(targetBase.toPlainString().take(digit.toInt())).toInt())

        return GannSquareResult(base,
                gannSquareResult.upTrendLevel.map { it.multiply(BigDecimal.TEN.pow(remainingNumberOfDigit)).divide(BigDecimal.TEN.pow(numberOfDecimalPoint.toInt())) }.toList(),
                gannSquareResult.downTrendLevel.map { it.multiply(BigDecimal.TEN.pow(remainingNumberOfDigit)).divide(BigDecimal.TEN.pow(numberOfDecimalPoint.toInt())) }.toList()
        )
    }

    override fun gannSquareResultOf(base: Int): GannSquareResult {
        if ( base >= maxBase ) throw Exception("${base} cannot be larger than ${maxBase}")
        val gannCell = gannCellMap[base]!!
        val first = firstLevelGannSquareOf(gannCell)
        val second = gannCell.secondLevelGannCell!!
        val secondUp = second.upThirdGannCell
        val secondDown = second.downThirdGannCell
        val third = thirdGannSqaureOf(gannCell)
        return GannSquareResult(
                base.toBigDecimal(),
                listOf(
                        first.first!!.base.toBigDecimal(),
                        secondUp!!.base.toBigDecimal(),
                        third.first!!.base.toBigDecimal()
                ),
                listOf(
                        first.second!!.base.toBigDecimal(),
                        secondDown!!.base.toBigDecimal(),
                        third.second!!.base.toBigDecimal()
                )
        )
    }

    private inline fun thirdGannSqaureOf(target: GannCell) : Pair<GannCell?, GannCell?> {
        val second = target.secondLevelGannCell!!
        var upThird : GannCell? = null
        var downThird : GannCell? = null
        var reflectionSpecialGann = specialGannCellsAsc.filterIsInstance(second.reflectionType).first { it.level ==  target.level}
        when ( second ) {
            is DiagonalCell -> {
                if ( reflectionSpecialGann is DownLeftDiagonalCell ) {
                    val diff = target.differenceFromSecondGannCell
                    if (diff < 0) {
                        val levelGannCells = levelCache[target.level]?.get(diff.absoluteValue-1)
                        if (levelGannCells?.base!! > second.upThirdGannCell?.base!!) {
                            upThird = levelGannCells
                            downThird = levelCache[target.level - 1]?.get(diff.absoluteValue-1)
                        } else {
                            downThird = levelGannCells
                            upThird = levelCache[target.level + 1]?.get(diff.absoluteValue-1)
                        }
                    } else if (diff > 0) {
                        val levelGannCells = levelCache[target.level]?.takeLast(diff.absoluteValue+1)?.first()
                        if (levelGannCells?.base!! > second.upThirdGannCell?.base!!) {
                            upThird = levelGannCells
                            downThird = levelCache[target.level - 1]?.takeLast(diff.absoluteValue+1)?.first()
                        } else {
                            downThird = levelGannCells
                            upThird = levelCache[target.level - 1]?.takeLast(diff.absoluteValue+1)?.first()
                        }
                    } else {
                        val levelGannCells = reflectionSpecialGann
                        if (levelGannCells?.base!! > second.upThirdGannCell?.base!!) {
                            upThird = levelGannCells
                            downThird = specialGannCellsAsc.filterIsInstance(second.reflectionType).first { it.level == target.level - 1 }
                        } else {
                            downThird = levelGannCells
                            upThird = specialGannCellsAsc.filterIsInstance(second.reflectionType).first { it.level == target.level + 1 }
                        }
                    }
                } else{
                   var levelGannCells = gannCellMap[reflectionSpecialGann.base - target.differenceFromSecondGannCell]
                    if (levelGannCells?.base!! > second.upThirdGannCell?.base!!) {
                        upThird = levelGannCells
                        reflectionSpecialGann = specialGannCellsAsc.filterIsInstance(second.reflectionType).first { it.level == target.level - 1 }
                        downThird = gannCellMap[reflectionSpecialGann.base - target.differenceFromSecondGannCell]
                    } else {
                        downThird = levelGannCells
                        reflectionSpecialGann = specialGannCellsAsc.filterIsInstance(second.reflectionType).first { it.level == target.level + 1 }
                        upThird = gannCellMap[reflectionSpecialGann.base - target.differenceFromSecondGannCell]
                    }
                }
                return Pair(upThird, downThird)
            }
            is CrossCell -> {
                var levelGannCells = gannCellMap[reflectionSpecialGann.base - target.differenceFromSecondGannCell]
                if (levelGannCells?.base!! > second.upThirdGannCell?.base!!) {
                    upThird = levelGannCells
                    reflectionSpecialGann = specialGannCellsAsc.filterIsInstance(second.reflectionType).first { it.level == target.level - 1 }
                    downThird = gannCellMap[reflectionSpecialGann.base - target.differenceFromSecondGannCell]
                } else {
                    downThird = levelGannCells
                    reflectionSpecialGann = specialGannCellsAsc.filterIsInstance(second.reflectionType).first { it.level == target.level + 1 }
                    upThird = gannCellMap[reflectionSpecialGann.base - target.differenceFromSecondGannCell]
                }
                return Pair(upThird, downThird)
            }
        }
        return Pair(null, null)
    }


    private inline fun firstLevelGannSquareOf(target : GannCell) =
        Pair(
                specialGannCellsAsc.filter { it.base > target.base + 2 }.firstOrNull(),
                specialGannCellsDesc.filter { it.base < target.base - 2 }.firstOrNull()
        )


//    private inline fun secondLevelGannSquareOf(target : GannCell) : Pair<GannSpecialCell?,GannSpecialCell?> {
//        val baseLevel = secondLevelBaseGannSquareOf(target)
//        var up : GannSpecialCell? = null
//        var down : GannSpecialCell? = null
//        when(baseLevel) {
//            is DiagonalCell -> {
//                val upNext = specialGannCellsAsc.filterIsInstance<DiagonalCell>().filter { it.level > baseLevel.level }.first()
//                if ( baseLevel.base > upNext.base )
//                    up = specialGannCellsAsc.filterIsInstance(upNext::class.java).filter { it.base > upNext.base }.first()
//                else
//                    up = upNext
//                val downNext = specialGannCellsDesc.filterIsInstance<DiagonalCell>().filter { it.level > baseLevel.level }.first()
//                if ( baseLevel.base < downNext.base )
//                    down = specialGannCellsAsc.filterIsInstance(downNext::class.java).filter { it.base < downNext.base }.first()
//                else
//                    down = downNext
//            }
//            is CrossCell -> {
//                val upNext = specialGannCellsAsc.filterIsInstance<CrossCell>().filter { it.level > baseLevel.level }.first()
//                if ( baseLevel.base > upNext.base )
//                    up = specialGannCellsAsc.filterIsInstance(upNext::class.java).filter { it.base > upNext.base }.first()
//                else
//                    up = upNext
//                val downNext = specialGannCellsDesc.filterIsInstance<CrossCell>().filter { it.level > baseLevel.level }.first()
//                if ( baseLevel.base < downNext.base )
//                    down = specialGannCellsAsc.filterIsInstance(downNext::class.java).filter { it.base < downNext.base }.first()
//                else
//                    down = downNext
//            }
//        }
//        return Pair(up, down)
//    }

//    private inline fun nextUp

//    private inline fun secondLevelBaseGannSquareOf(target : GannCell) : GannSpecialCell {
//        var nearestSpecialGannSquare = arrayListOf(upTrendSpecialGannCell(target),downTrendSpecialGannCell(target))
//        if ( nearestSpecialGannSquare.any {it is DiagonalCell} && nearestSpecialGannSquare.any {it is CrossCell} ) {
//            var diagonalCell = nearestSpecialGannSquare.first {it is DiagonalCell}
//            if ( (diagonalCell!!.base - 2 .. diagonalCell!!.base + 2 ).contains(target.base) ) return diagonalCell
//            return nearestSpecialGannSquare.first {it is CrossCell}!!
//        } else throw RuntimeException("${target.base} do not have enough special gann cell")
//    }
//
//    private inline fun upTrendSpecialGannCell(target : GannCell) : GannSpecialCell? =
//            specialGannCellsAsc.filter {it.base > target.base}.firstOrNull()
//
//    private inline fun downTrendSpecialGannCell(target : GannCell) : GannSpecialCell? =
//            specialGannCellsDesc.filter {it.base < target.base}.firstOrNull()



}



