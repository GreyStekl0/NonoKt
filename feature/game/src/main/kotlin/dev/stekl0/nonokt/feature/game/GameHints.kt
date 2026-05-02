package dev.stekl0.nonokt.feature.game

import dev.stekl0.nonokt.core.model.GameLevel
import kotlinx.collections.immutable.toPersistentList

internal fun lineHint(line: String): LineHint =
    buildLineHint(
        length = line.length,
        cellAt = line::get,
    )

internal fun columnHint(
    level: GameLevel,
    column: Int,
): LineHint =
    run {
        require(column in 0 until level.size) {
            "Column must be in 0 until ${level.size}, was: $column."
        }
        buildLineHint(
            length = level.size,
            cellAt = { row -> level.solution[row][column] },
        )
    }

private inline fun buildLineHint(
    length: Int,
    cellAt: (Int) -> Char,
): LineHint {
    val values =
        buildList {
            var runLength = 0

            repeat(length) { index ->
                val cell = cellAt(index)
                if (cell == '1') {
                    runLength += 1
                } else if (runLength > 0) {
                    add(runLength)
                    runLength = 0
                }
            }

            if (runLength > 0) add(runLength)
        }

    return LineHint(
        values = values.ifEmpty { listOf(0) }.toPersistentList(),
    )
}
