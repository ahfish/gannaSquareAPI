package gannaSquareAPI.domain.gannSquare

import java.math.BigDecimal

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

open class GannSpecialCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannSpecialCell? = null,
        open var upThirdGannCell: GannSpecialCell? = null,
        open var downThirdGannCell: GannSpecialCell? = null) : GannCell(base, level, secondLevelGannCell) {

    override fun toString(): String =
        "${super.toString()} Third [Up - ${upThirdGannCell?.base}, Down - ${downThirdGannCell?.base}}"

}

open class GannCell(
        open val base: Int,
        open val level: Int,
        open var differenceFromSecondGannCell: Int,
) {
    constructor(base: Int, level: Int, secondLevelGannCell: GannSpecialCell?) : this(base, level, base-secondLevelGannCell?.base!!) {
        this.secondLevelGannCell = secondLevelGannCell
    }

    constructor(base: Int, level: Int, differenceFromSecondGannCell: Int, secondLevelGannCell: GannSpecialCell?) : this(base, level, differenceFromSecondGannCell) {
        this.secondLevelGannCell = secondLevelGannCell
    }

    open var secondLevelGannCell: GannSpecialCell? = null

    override fun toString() =
            "gannCell Type ${this.javaClass.name}, base ${base}, level ${level}, second gann cell [${secondLevelGannCell?.base}]"
}


open class NormalCell(
        override val base: Int,
        override val level: Int,
        val direction: Direction,
        override var secondLevelGannCell: GannSpecialCell? = null
) : GannCell(base, level, secondLevelGannCell) {
    constructor(base: Int, level: Int, direction: Direction, differenceFromSecondGannCell: Int, secondLevelGannCell: GannSpecialCell?) : this(base, level, direction, secondLevelGannCell) {
        super.differenceFromSecondGannCell = differenceFromSecondGannCell
    }

}


open class CrossCell(
        override val base: Int,
        override val level: Int,
//        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell: GannSpecialCell? = null,
        override var downThirdGannCell: GannSpecialCell? = null
) : GannSpecialCell(base, level, upThirdGannCell, downThirdGannCell) {
    override var secondLevelGannCell: GannSpecialCell? = null
        get() = this

}

open class DiagonalCell(
        override val base: Int,
        override val level: Int,
//        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell: GannSpecialCell? = null,
        override var downThirdGannCell: GannSpecialCell? = null,
        val reflectionType : Class<out DiagonalCell>
) : GannSpecialCell(base, level, upThirdGannCell, downThirdGannCell) {
    override var secondLevelGannCell: GannSpecialCell? = null
        get() = this
}

open class UpRightDiagonalCell(
        override val base: Int,
        override val level: Int,
//        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell: GannSpecialCell? = null,
        override var downThirdGannCell: GannSpecialCell? = null
) : DiagonalCell(base, level, upThirdGannCell, downThirdGannCell, DownLeftDiagonalCell::class.java)

open class DownRightDiagonalCell(
        override val base: Int,
        override val level: Int,
//        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell: GannSpecialCell? = null,
        override var downThirdGannCell: GannSpecialCell? = null
) : DiagonalCell(base, level, upThirdGannCell, downThirdGannCell, UpLeftDiagonalCell::class.java)

open class UpLeftDiagonalCell(
        override val base: Int,
        override val level: Int,
//        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell: GannSpecialCell? = null,
        override var downThirdGannCell: GannSpecialCell? = null
) : DiagonalCell(base, level, upThirdGannCell, downThirdGannCell, DownRightDiagonalCell::class.java)


open class DownLeftDiagonalCell(
        override val base: Int,
        override val level: Int,
//        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell: GannSpecialCell? = null,
        override var downThirdGannCell: GannSpecialCell? = null
) : DiagonalCell(base, level, upThirdGannCell, downThirdGannCell, UpLeftDiagonalCell::class.java)

open class UpCrossCell(
        override val base: Int,
        override val level: Int,
//        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell: GannSpecialCell? = null,
        override var downThirdGannCell: GannSpecialCell? = null
) : CrossCell(base, level, upThirdGannCell, downThirdGannCell)

open class RightCrossCell(
        override val base: Int,
        override val level: Int,
//        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell: GannSpecialCell? = null,
        override var downThirdGannCell: GannSpecialCell? = null
) : CrossCell(base, level, upThirdGannCell, downThirdGannCell)

open class LeftCrossCell(
        override val base: Int,
        override val level: Int,
//        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell: GannSpecialCell? = null,
        override var downThirdGannCell: GannSpecialCell? = null
) : CrossCell(base, level, upThirdGannCell, downThirdGannCell)

open class DownCrossCell(
        override val base: Int,
        override val level: Int,
//        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell: GannSpecialCell? = null,
        override var downThirdGannCell: GannSpecialCell? = null
) : CrossCell(base, level, upThirdGannCell, downThirdGannCell)


open class GannSquareResult(
        val upTrendLevel: List<BigDecimal>,
        val downTrendLevel: List<BigDecimal>
)