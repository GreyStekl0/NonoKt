package dev.stekl0.nonokt.feature.game.ui

import androidx.compose.ui.unit.Dp

internal data class GameBoardLayout(
    val boardSize: Int,
    val cellSize: Dp,
    val boardWidth: Dp,
    val boardHeight: Dp,
    val rowHintWidth: Dp,
    val columnHintHeight: Dp,
)

internal fun calculateBoardLayout(
    maxWidth: Dp,
    maxHeight: Dp,
    boardSize: Int,
    maxRowHintCount: Int,
    maxColumnHintCount: Int,
): GameBoardLayout {
    require(maxWidth.value > 0f && maxHeight.value > 0f) {
        "Board layout constraints must be positive."
    }
    require(boardSize > 0) {
        "Board size must be positive."
    }
    require(maxRowHintCount >= 0 && maxColumnHintCount >= 0) {
        "Board hint counts must not be negative."
    }

    val totalColumns = maxRowHintCount + boardSize
    val totalRows = maxColumnHintCount + boardSize
    val cellSize = minOf(maxWidth / totalColumns, maxHeight / totalRows)

    return GameBoardLayout(
        boardSize = boardSize,
        cellSize = cellSize,
        boardWidth = cellSize * boardSize,
        boardHeight = cellSize * boardSize,
        rowHintWidth = cellSize * maxRowHintCount,
        columnHintHeight = cellSize * maxColumnHintCount,
    )
}
