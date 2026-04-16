package dev.stekl0.nonokt.feature.game.impl.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.stekl0.nonokt.feature.game.impl.GameState
import dev.stekl0.nonokt.feature.game.impl.LineHint
import dev.stekl0.nonokt.feature.game.impl.PlayerCellState

private val ThinGridStroke: Dp = 0.75.dp
private val ThickGridStroke: Dp = 1.75.dp
private val BoardPadding: Dp = 16.dp

@Composable
internal fun NonogramBoard(
    state: GameState,
    onCellPress: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        BoxWithConstraints(
            modifier = Modifier.padding(BoardPadding),
            contentAlignment = Alignment.Center,
        ) {
            val hintCompletion =
                remember(state.board, state.rowHints, state.columnHints) {
                    state.buildHintCompletion()
                }
            val layout =
                calculateBoardLayout(
                    maxWidth = maxWidth,
                    maxHeight = maxHeight,
                    boardSize = state.level.size,
                    maxRowHintCount = state.maxRowHintCount,
                    maxColumnHintCount = state.maxColumnHintCount,
                )

            Column(
                modifier = Modifier.width(layout.rowHintWidth + layout.boardWidth),
            ) {
                Row(modifier = Modifier.height(layout.columnHintHeight)) {
                    HintCorner(
                        width = layout.rowHintWidth,
                        height = layout.columnHintHeight,
                    )
                    ColumnHints(
                        state = state,
                        completion = hintCompletion,
                        boardSize = layout.boardSize,
                        cellSize = layout.cellSize,
                    )
                }
                Row(modifier = Modifier.height(layout.boardHeight)) {
                    RowHints(
                        state = state,
                        completion = hintCompletion,
                        boardSize = layout.boardSize,
                        cellSize = layout.cellSize,
                    )
                    BoardGrid(
                        state = state,
                        boardSize = layout.boardSize,
                        cellSize = layout.cellSize,
                        onCellPress = onCellPress,
                    )
                }
            }
        }
    }
}

@Composable
private fun HintCorner(
    width: Dp,
    height: Dp,
) {
    Box(
        modifier =
            Modifier
                .width(width)
                .height(height)
                .drawHintDecoration(
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    strokes =
                        CellStrokeWidths(
                            top = ThickGridStroke,
                            right = ThickGridStroke,
                            bottom = ThickGridStroke,
                            left = ThickGridStroke,
                        ),
                    lineColor = MaterialTheme.colorScheme.outline,
                ),
    )
}

@Composable
private fun ColumnHints(
    state: GameState,
    completion: HintCompletion,
    boardSize: Int,
    cellSize: Dp,
) {
    Row(
        modifier = Modifier.width(cellSize * boardSize),
    ) {
        state.columnHints.forEachIndexed { columnIndex, hint ->
            ColumnHint(
                hint = hint,
                completion = completion.columns[columnIndex],
                columnIndex = columnIndex,
                columnCount = boardSize,
                cellSize = cellSize,
            )
        }
    }
}

@Composable
private fun ColumnHint(
    hint: LineHint,
    completion: HintCompletionLine,
    columnIndex: Int,
    columnCount: Int,
    cellSize: Dp,
) {
    val colorScheme = MaterialTheme.colorScheme
    val hintRowCount = completion.values.size
    val values =
        remember(
            hint,
            hintRowCount,
        ) { List(hintRowCount - hint.values.size) { null } + hint.values }

    Column(
        modifier = Modifier.width(cellSize),
    ) {
        values.forEachIndexed { hintRowIndex, value ->
            HintCell(
                value = value,
                modifier = Modifier.size(cellSize),
                textColor =
                    hintTextColor(
                        isCompleted = completion.values[hintRowIndex],
                        colorScheme = colorScheme,
                    ),
                strokes =
                    CellStrokeWidths(
                        top = if (hintRowIndex == 0) ThickGridStroke else ThinGridStroke,
                        right =
                            trailingVerticalStroke(
                                columnIndex = columnIndex,
                                columnCount = columnCount,
                                thinGridStroke = ThinGridStroke,
                                thickGridStroke = ThickGridStroke,
                            ),
                        bottom = if (hintRowIndex == hintRowCount - 1) ThickGridStroke else ThinGridStroke,
                        left =
                            leadingVerticalStroke(
                                columnIndex = columnIndex,
                                thinGridStroke = ThinGridStroke,
                                thickGridStroke = ThickGridStroke,
                            ),
                    ),
            )
        }
    }
}

@Composable
private fun RowHints(
    state: GameState,
    completion: HintCompletion,
    boardSize: Int,
    cellSize: Dp,
) {
    Column(
        modifier = Modifier.width(cellSize * state.maxRowHintCount),
    ) {
        state.rowHints.forEachIndexed { rowIndex, hint ->
            RowHint(
                hint = hint,
                completion = completion.rows[rowIndex],
                rowIndex = rowIndex,
                rowCount = boardSize,
                cellSize = cellSize,
            )
        }
    }
}

@Composable
private fun RowHint(
    hint: LineHint,
    completion: HintCompletionLine,
    rowIndex: Int,
    rowCount: Int,
    cellSize: Dp,
) {
    val colorScheme = MaterialTheme.colorScheme
    val hintColumnCount = completion.values.size
    val values =
        remember(
            hint,
            hintColumnCount,
        ) { List(hintColumnCount - hint.values.size) { null } + hint.values }

    Row(
        modifier = Modifier.height(cellSize),
    ) {
        values.forEachIndexed { hintColumnIndex, value ->
            HintCell(
                value = value,
                modifier = Modifier.size(cellSize),
                textColor =
                    hintTextColor(
                        isCompleted = completion.values[hintColumnIndex],
                        colorScheme = colorScheme,
                    ),
                strokes =
                    CellStrokeWidths(
                        top =
                            leadingHorizontalStroke(
                                rowIndex = rowIndex,
                                thinGridStroke = ThinGridStroke,
                                thickGridStroke = ThickGridStroke,
                            ),
                        right = if (hintColumnIndex == hintColumnCount - 1) ThickGridStroke else ThinGridStroke,
                        bottom =
                            trailingHorizontalStroke(
                                rowIndex = rowIndex,
                                rowCount = rowCount,
                                thinGridStroke = ThinGridStroke,
                                thickGridStroke = ThickGridStroke,
                            ),
                        left = if (hintColumnIndex == 0) ThickGridStroke else ThinGridStroke,
                    ),
            )
        }
    }
}

@Composable
private fun BoardGrid(
    state: GameState,
    boardSize: Int,
    cellSize: Dp,
    onCellPress: (Int, Int) -> Unit,
) {
    Column(
        modifier = Modifier.width(cellSize * boardSize),
    ) {
        state.board.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.height(cellSize),
            ) {
                row.forEachIndexed { columnIndex, cellState ->
                    BoardCell(
                        cellState = cellState,
                        modifier = Modifier.size(cellSize),
                        enabled = state.isInteractionEnabled,
                        strokes =
                            CellStrokeWidths(
                                top =
                                    leadingHorizontalStroke(
                                        rowIndex = rowIndex,
                                        thinGridStroke = ThinGridStroke,
                                        thickGridStroke = ThickGridStroke,
                                    ),
                                right =
                                    trailingVerticalStroke(
                                        columnIndex = columnIndex,
                                        columnCount = boardSize,
                                        thinGridStroke = ThinGridStroke,
                                        thickGridStroke = ThickGridStroke,
                                    ),
                                bottom =
                                    trailingHorizontalStroke(
                                        rowIndex = rowIndex,
                                        rowCount = boardSize,
                                        thinGridStroke = ThinGridStroke,
                                        thickGridStroke = ThickGridStroke,
                                    ),
                                left =
                                    leadingVerticalStroke(
                                        columnIndex = columnIndex,
                                        thinGridStroke = ThinGridStroke,
                                        thickGridStroke = ThickGridStroke,
                                    ),
                            ),
                        onClick = { onCellPress(rowIndex, columnIndex) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HintCell(
    value: Int?,
    textColor: Color,
    strokes: CellStrokeWidths,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier.drawHintDecoration(
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                strokes = strokes,
                lineColor = MaterialTheme.colorScheme.outline,
            ),
        contentAlignment = Alignment.Center,
    ) {
        value?.let {
            Text(
                text = it.toString(),
                color = textColor,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun BoardCell(
    cellState: PlayerCellState,
    enabled: Boolean,
    strokes: CellStrokeWidths,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val backgroundColor =
        when (cellState) {
            PlayerCellState.EMPTY -> colorScheme.surfaceContainerLowest
            PlayerCellState.FILLED -> colorScheme.onSurface
            PlayerCellState.MARKED -> colorScheme.surfaceContainerLowest
            PlayerCellState.ERROR -> colorScheme.errorContainer
        }

    val markerColor =
        when (cellState) {
            PlayerCellState.MARKED -> colorScheme.onSurface
            PlayerCellState.ERROR -> colorScheme.error
            else -> Color.Unspecified
        }

    Box(
        modifier =
            modifier
                .clickable(enabled = enabled, onClick = onClick)
                .drawBoardDecoration(
                    backgroundColor = backgroundColor,
                    strokes = strokes,
                    lineColor = colorScheme.outline,
                    markerColor = markerColor,
                ),
    )
}
