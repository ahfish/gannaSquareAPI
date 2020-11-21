package gannaSquareAPI.domain.gannSquare

import java.math.BigDecimal

open class GannCell(
        open val base: Int,
        open val level: Int,
        open val next: GannCell? = null,
        open val previous: GannCell? = null
)

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
) : GannCell(base, level, next, previous)

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

open class UpCorssCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : CrossCell(base, level, next, previous)

open class RightCorssCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : CrossCell(base, level, next, previous)

open class LeftCorssCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : CrossCell(base, level, next, previous)

open class DownCorssCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : CrossCell(base, level, next, previous)



open class GannSquareResult(
        val upTrendLevel: List<BigDecimal>,
        val downTrendLevel: List<BigDecimal>
)