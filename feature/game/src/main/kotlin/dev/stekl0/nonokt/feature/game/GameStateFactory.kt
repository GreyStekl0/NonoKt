package dev.stekl0.nonokt.feature.game

import dev.stekl0.nonokt.core.model.GameLevel
import kotlinx.collections.immutable.toPersistentList

internal fun createGameState(level: GameLevel): GameState {
    val size = level.size
    val rowHints = level.solution.map(::lineHint).toPersistentList()
    val columnHints =
        (0 until size)
            .map { column -> columnHint(level, column) }
            .toPersistentList()
    val requiredFilledCellCount = rowHints.sumOf { hint -> hint.values.sum() }
    require(requiredFilledCellCount == columnHints.sumOf { hint -> hint.values.sum() }) {
        "Row and column hints must be consistent."
    }

    return GameState(
        level = level,
        board =
            List(size = size) {
                List(size = size) { PlayerCellState.EMPTY }.toPersistentList()
            }.toPersistentList(),
        rowHints = rowHints,
        columnHints = columnHints,
        maxRowHintCount = rowHints.maxOfOrNull(LineHint::size) ?: 0,
        maxColumnHintCount = columnHints.maxOfOrNull(LineHint::size) ?: 0,
        requiredFilledCellCount = requiredFilledCellCount,
    )
}
