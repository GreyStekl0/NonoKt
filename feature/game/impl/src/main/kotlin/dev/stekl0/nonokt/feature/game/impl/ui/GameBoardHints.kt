package dev.stekl0.nonokt.feature.game.impl.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import dev.stekl0.nonokt.feature.game.impl.LineHint
import kotlinx.collections.immutable.ImmutableList

private enum class HintOrientation {
    COLUMN,
    ROW,
}

@Composable
internal fun ColumnHints(
    hints: ImmutableList<LineHint>,
    completions: ImmutableList<HintCompletionLine>,
    boardSize: Int,
    cellSize: Dp,
) {
    HintsGrid(
        orientation = HintOrientation.COLUMN,
        hints = hints,
        completions = completions,
        lineCount = boardSize,
        maxHintCount = boardSize,
        cellSize = cellSize,
    )
}

@Composable
internal fun RowHints(
    hints: ImmutableList<LineHint>,
    completions: ImmutableList<HintCompletionLine>,
    maxHintCount: Int,
    boardSize: Int,
    cellSize: Dp,
) {
    HintsGrid(
        orientation = HintOrientation.ROW,
        hints = hints,
        completions = completions,
        lineCount = boardSize,
        maxHintCount = maxHintCount,
        cellSize = cellSize,
    )
}

@Composable
private fun HintsGrid(
    orientation: HintOrientation,
    hints: ImmutableList<LineHint>,
    completions: ImmutableList<HintCompletionLine>,
    lineCount: Int,
    maxHintCount: Int,
    cellSize: Dp,
) {
    when (orientation) {
        HintOrientation.COLUMN -> {
            Row(modifier = Modifier.width(cellSize * lineCount)) {
                hints.forEachIndexed { lineIndex, hint ->
                    HintLine(
                        orientation = orientation,
                        hint = hint,
                        completion = completions[lineIndex],
                        lineIndex = lineIndex,
                        lineCount = lineCount,
                        cellSize = cellSize,
                    )
                }
            }
        }

        HintOrientation.ROW -> {
            Column(modifier = Modifier.width(cellSize * maxHintCount)) {
                hints.forEachIndexed { lineIndex, hint ->
                    HintLine(
                        orientation = orientation,
                        hint = hint,
                        completion = completions[lineIndex],
                        lineIndex = lineIndex,
                        lineCount = lineCount,
                        cellSize = cellSize,
                    )
                }
            }
        }
    }
}

@Composable
private fun HintLine(
    orientation: HintOrientation,
    hint: LineHint,
    completion: HintCompletionLine,
    lineIndex: Int,
    lineCount: Int,
    cellSize: Dp,
) {
    val colorScheme = MaterialTheme.colorScheme
    val hintCount = completion.values.size
    val values =
        remember(hint.values, hintCount) {
            paddedHintValues(
                values = hint.values,
                targetSize = hintCount,
            )
        }

    when (orientation) {
        HintOrientation.COLUMN -> {
            Column(modifier = Modifier.width(cellSize)) {
                HintCells(
                    orientation = orientation,
                    values = values,
                    completion = completion,
                    lineIndex = lineIndex,
                    lineCount = lineCount,
                    textColor = { isCompleted -> hintTextColor(isCompleted, colorScheme) },
                    cellSize = cellSize,
                )
            }
        }

        HintOrientation.ROW -> {
            Row(modifier = Modifier.height(cellSize)) {
                HintCells(
                    orientation = orientation,
                    values = values,
                    completion = completion,
                    lineIndex = lineIndex,
                    lineCount = lineCount,
                    textColor = { isCompleted -> hintTextColor(isCompleted, colorScheme) },
                    cellSize = cellSize,
                )
            }
        }
    }
}

@Composable
private fun HintCells(
    orientation: HintOrientation,
    values: ImmutableList<Int?>,
    completion: HintCompletionLine,
    lineIndex: Int,
    lineCount: Int,
    textColor: (Boolean) -> Color,
    cellSize: Dp,
) {
    values.forEachIndexed { valueIndex, value ->
        HintCell(
            value = value,
            modifier = Modifier.size(cellSize),
            textColor = textColor(completion.values[valueIndex]),
            strokes =
                hintCellStrokes(
                    orientation = orientation,
                    lineIndex = lineIndex,
                    lineCount = lineCount,
                    valueIndex = valueIndex,
                    valueCount = values.size,
                ),
        )
    }
}

private fun hintCellStrokes(
    orientation: HintOrientation,
    lineIndex: Int,
    lineCount: Int,
    valueIndex: Int,
    valueCount: Int,
): CellStrokeWidths =
    when (orientation) {
        HintOrientation.COLUMN -> {
            columnHintCellStrokes(
                hintRowIndex = valueIndex,
                hintRowCount = valueCount,
                columnIndex = lineIndex,
                columnCount = lineCount,
            )
        }

        HintOrientation.ROW -> {
            rowHintCellStrokes(
                rowIndex = lineIndex,
                rowCount = lineCount,
                hintColumnIndex = valueIndex,
                hintColumnCount = valueCount,
            )
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
