package gannaSquareAPI.domain.gannSquare

import java.math.BigDecimal

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

open class GannSpecialCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        open var upThirdGannCell : GannSpecialCell? = null,
        open  var downThirdGannCell : GannSpecialCell? = null) : GannCell(base, level, secondLevelGannCell)

open class GannCell(
        open val base: Int,
        open val level: Int
) {
    constructor(base: Int, level: Int, secondLevelGannCell: GannCell?) : this(base, level) {
        this.secondLevelGannCell= secondLevelGannCell
    }

    open var secondLevelGannCell: GannCell? = null
        get() = secondLevelGannCell?: this
    override fun toString() =
            "gannCell Type ${this.javaClass.name}, base ${base}, level ${level}, second gann cell [${secondLevelGannCell?.base}]"
}

open class UpCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null
) : GannCell(base, level, secondLevelGannCell)

open class LeftCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null
) : GannCell(base, level, secondLevelGannCell)

open class NormalCell(
        override val base: Int,
        override val level: Int,
        val direction : Direction,
        override var secondLevelGannCell: GannCell? = null
) : GannCell(base, level, secondLevelGannCell)

open class DownCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null
) : GannCell(base, level, secondLevelGannCell)

open class CrossCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell : GannSpecialCell? = null,
        override var downThirdGannCell : GannSpecialCell? = null
) : GannSpecialCell(base, level, secondLevelGannCell, upThirdGannCell, downThirdGannCell)

open class DiagonalCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell : GannSpecialCell? = null,
        override var downThirdGannCell : GannSpecialCell? = null
) : GannSpecialCell(base, level, secondLevelGannCell, upThirdGannCell, downThirdGannCell)

open class UpRightDiagonalCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell : GannSpecialCell? = null,
        override var downThirdGannCell : GannSpecialCell? = null
) : DiagonalCell(base, level, secondLevelGannCell, upThirdGannCell, downThirdGannCell)

open class DownRightDiagonalCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell : GannSpecialCell? = null,
        override var downThirdGannCell : GannSpecialCell? = null
) : DiagonalCell(base, level, secondLevelGannCell, upThirdGannCell, downThirdGannCell)

open class UpLeftDiagonalCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell : GannSpecialCell? = null,
        override var downThirdGannCell : GannSpecialCell? = null
) : DiagonalCell(base, level, secondLevelGannCell, upThirdGannCell, downThirdGannCell)


open class DownLeftDiagonalCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell : GannSpecialCell? = null,
        override var downThirdGannCell : GannSpecialCell? = null
) : DiagonalCell(base, level, secondLevelGannCell, upThirdGannCell, downThirdGannCell)

open class UpCrossCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell : GannSpecialCell? = null,
        override var downThirdGannCell : GannSpecialCell? = null
) : CrossCell(base, level, secondLevelGannCell, upThirdGannCell, downThirdGannCell)

open class RightCrossCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell : GannSpecialCell? = null,
        override var downThirdGannCell : GannSpecialCell? = null
) : CrossCell(base, level, secondLevelGannCell, upThirdGannCell, downThirdGannCell)

open class LeftCrossCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell : GannSpecialCell? = null,
        override var downThirdGannCell : GannSpecialCell? = null
) : CrossCell(base, level, secondLevelGannCell, upThirdGannCell, downThirdGannCell)

open class DownCrossCell(
        override val base: Int,
        override val level: Int,
        override var secondLevelGannCell: GannCell? = null,
        override var upThirdGannCell : GannSpecialCell? = null,
        override var downThirdGannCell : GannSpecialCell? = null
) : CrossCell(base, level, secondLevelGannCell, upThirdGannCell, downThirdGannCell)


open class GannSquareResult(
        val upTrendLevel: List<BigDecimal>,
        val downTrendLevel: List<BigDecimal>
)