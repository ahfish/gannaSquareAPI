package gannaSquareAPI.domain.gannSquare

import java.math.BigDecimal

open class GannCell(
        open val base: Int,
        open val level: Int,
        open val next: GannCell? = null,
        open val previous: GannCell? = null
)

data class CrossCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : GannCell(base, level, next, previous)

data class DiagonalCell(
        override val base: Int,
        override val level: Int,
        override val next: GannCell? = null,
        override val previous: GannCell? = null
) : GannCell(base, level, next, previous)


data class GannSquareResult(
        val upTrendLevel: List<BigDecimal>,
        val downTrendLevel: List<BigDecimal>
)