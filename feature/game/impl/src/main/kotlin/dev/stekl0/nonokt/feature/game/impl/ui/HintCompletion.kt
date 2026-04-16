package dev.stekl0.nonokt.feature.game.impl.ui

import dev.stekl0.nonokt.feature.game.impl.LineHint
import dev.stekl0.nonokt.feature.game.impl.PlayerCellState

internal data class HintCompletion(
    val rows: List<List<Boolean>>,
    val columns: List<List<Boolean>>,
)

internal fun calculateHintCompletion(
    board: List<List<PlayerCellState>>,
    rowHints: List<LineHint>,
    columnHints: List<LineHint>,
): HintCompletion {
    val rows =
        rowHints.mapIndexed { rowIndex, hint ->
            clueCompletion(
                hintValues = hint.values,
                runs = filledRunsInRow(board[rowIndex]),
            )
        }

    val columns =
        columnHints.mapIndexed { columnIndex, hint ->
            clueCompletion(
                hintValues = hint.values,
                runs = filledRunsInColumn(board = board, columnIndex = columnIndex),
            )
        }

    return HintCompletion(rows = rows, columns = columns)
}

private fun filledRunsInRow(row: List<PlayerCellState>): List<Int> = filledRuns(length = row.size) { index -> row[index] }

private fun filledRunsInColumn(
    board: List<List<PlayerCellState>>,
    columnIndex: Int,
): List<Int> = filledRuns(length = board.size) { rowIndex -> board[rowIndex][columnIndex] }

private inline fun filledRuns(
    length: Int,
    cellAt: (Int) -> PlayerCellState,
): List<Int> {
    var runLength = 0
    val runs =
        buildList {
            repeat(length) { index ->
                if (cellAt(index) == PlayerCellState.FILLED) {
                    runLength += 1
                } else if (runLength > 0) {
                    add(runLength)
                    runLength = 0
                }
            }

            if (runLength > 0) add(runLength)
        }

    return runs.ifEmpty { listOf(0) }
}

private fun clueCompletion(
    hintValues: List<Int>,
    runs: List<Int>,
): List<Boolean> {
    var prefixMatched = true

    return hintValues.mapIndexed { index, value ->
        if (!prefixMatched) {
            false
        } else {
            val matched = runs.getOrNull(index) == value
            prefixMatched = matched
            matched
        }
    }
}
