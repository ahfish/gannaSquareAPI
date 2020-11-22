package gannaSquareAPI.domain.gannSquare

import java.math.BigDecimal

interface GannSpecialCell

open class GannCell(
        open val base: Int,
        open val level: Int,
        open val next: GannCell? = null,
        open val previous: GannCell? = null
) {
    override fun toString() =
        "gannCell Type ${this.javaClass.name}, base ${base}, level ${level}"
}

open class UpCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : GannCell(base, level, next, previous)

open class LeftCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : GannCell(base, level, next, previous)

open class RightCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : GannCell(base, level, next, previous)

open class DownCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : GannCell(base, level, next, previous)

open class CrossCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : GannCell(base, level, next, previous)

open class DiagonalCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : GannCell(base, level, next, previous), GannSpecialCell

open class UpRightDiagonalCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : DiagonalCell(base, level, next, previous)

open class DownRightDiagonalCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : DiagonalCell(base, level, next, previous)

open class UpLeftDiagonalCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : DiagonalCell(base, level, next, previous)


open class DownLeftDiagonalCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : DiagonalCell(base, level, next, previous)

open class UpCrossCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : UpCell(base, level, next, previous), GannSpecialCell

open class RightCrossCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : RightCell(base, level, next, previous), GannSpecialCell

open class LeftCrossCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : LeftCell(base, level, next, previous), GannSpecialCell

open class DownCrossCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : DownCell(base, level, next, previous), GannSpecialCell



open class GannSquareResult(
        val upTrendLevel: List<BigDecimal>,
        val downTrendLevel: List<BigDecimal>
)