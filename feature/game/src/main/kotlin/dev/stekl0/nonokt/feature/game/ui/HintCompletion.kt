package dev.stekl0.nonokt.feature.game.ui

import dev.stekl0.nonokt.feature.game.GameState
import dev.stekl0.nonokt.feature.game.LineHint
import dev.stekl0.nonokt.feature.game.PlayerCellState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

internal data class HintCompletionLine(
    val values: ImmutableList<Boolean>,
)

internal data class HintCompletion(
    val rows: ImmutableList<HintCompletionLine>,
    val columns: ImmutableList<HintCompletionLine>,
)

internal fun calculateHintCompletion(
    board: List<List<PlayerCellState>>,
    rowHints: List<LineHint>,
    columnHints: List<LineHint>,
    rowHintCount: Int,
    columnHintCount: Int,
): HintCompletion {
    val rows =
        rowHints
            .mapIndexed { rowIndex, hint ->
                HintCompletionLine(
                    values =
                        alignClueCompletion(
                            completion =
                                clueCompletion(
                                    hintValues = hint.values,
                                    runs = filledRunsInRow(board[rowIndex]),
                                ),
                            targetSize = rowHintCount,
                        ),
                )
            }.toPersistentList()

    val columns =
        columnHints
            .mapIndexed { columnIndex, hint ->
                HintCompletionLine(
                    values =
                        alignClueCompletion(
                            completion =
                                clueCompletion(
                                    hintValues = hint.values,
                                    runs =
                                        filledRunsInColumn(
                                            board = board,
                                            columnIndex = columnIndex,
                                        ),
                                ),
                            targetSize = columnHintCount,
                        ),
                )
            }.toPersistentList()

    return HintCompletion(rows = rows, columns = columns)
}

private fun filledRunsInRow(row: List<PlayerCellState>): List<Int> = filledRuns(length = row.size) { row[it] }

private fun filledRunsInColumn(
    board: List<List<PlayerCellState>>,
    columnIndex: Int,
): List<Int> = filledRuns(length = board.size) { board[it][columnIndex] }

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
): ImmutableList<Boolean> {
    var prefixMatched = true

    return hintValues
        .mapIndexed { index, value ->
            if (!prefixMatched) {
                false
            } else {
                val matched = runs.getOrNull(index) == value
                prefixMatched = matched
                matched
            }
        }.toPersistentList()
}

private fun alignClueCompletion(
    completion: ImmutableList<Boolean>,
    targetSize: Int,
): ImmutableList<Boolean> = (List(targetSize - completion.size) { false } + completion).toPersistentList()

internal fun GameState.buildHintCompletion(): HintCompletion =
    calculateHintCompletion(
        board = board,
        rowHints = rowHints,
        columnHints = columnHints,
        rowHintCount = maxRowHintCount,
        columnHintCount = maxColumnHintCount,
    )
