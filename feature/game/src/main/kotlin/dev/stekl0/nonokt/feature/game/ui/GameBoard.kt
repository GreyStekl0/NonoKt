package dev.stekl0.nonokt.feature.game.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.stekl0.nonokt.feature.game.GameState
import dev.stekl0.nonokt.feature.game.PlayerCellState
import dev.stekl0.nonokt.feature.game.R

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
                        hints = state.columnHints,
                        completions = hintCompletion.columns,
                        boardSize = layout.boardSize,
                        cellSize = layout.cellSize,
                    )
                }
                Row(modifier = Modifier.height(layout.boardHeight)) {
                    RowHints(
                        hints = state.rowHints,
                        completions = hintCompletion.rows,
                        maxHintCount = state.maxRowHintCount,
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
                        rowIndex = rowIndex,
                        columnIndex = columnIndex,
                        modifier = Modifier.size(cellSize).testTag("game_cell:$rowIndex:$columnIndex"),
                        enabled = state.isInteractionEnabled,
                        strokes =
                            boardCellStrokes(
                                rowIndex = rowIndex,
                                rowCount = boardSize,
                                columnIndex = columnIndex,
                                columnCount = boardSize,
                            ),
                        onClick = { onCellPress(rowIndex, columnIndex) },
                    )
                }
            }
        }
    }
}

@Composable
private fun BoardCell(
    cellState: PlayerCellState,
    rowIndex: Int,
    columnIndex: Int,
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
    val cellDescription =
        stringResource(
            R.string.feature_game_cell_description,
            rowIndex + 1,
            columnIndex + 1,
        )
    val cellStateDescription = cellState.description()

    Box(
        modifier =
            modifier
                .semantics {
                    contentDescription = cellDescription
                    stateDescription = cellStateDescription
                }.clickable(
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                ).drawBoardDecoration(
                    backgroundColor = backgroundColor,
                    strokes = strokes,
                    lineColor = colorScheme.outline,
                    markerColor = markerColor,
                ),
    )
}

@Composable
private fun PlayerCellState.description(): String =
    when (this) {
        PlayerCellState.EMPTY -> stringResource(R.string.feature_game_cell_empty)
        PlayerCellState.FILLED -> stringResource(R.string.feature_game_cell_filled)
        PlayerCellState.MARKED -> stringResource(R.string.feature_game_cell_marked)
        PlayerCellState.ERROR -> stringResource(R.string.feature_game_cell_error)
    }
