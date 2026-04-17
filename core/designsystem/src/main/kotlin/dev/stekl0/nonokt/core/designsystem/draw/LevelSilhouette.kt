package dev.stekl0.nonokt.core.designsystem.draw

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
public fun levelSilhouetteColor(): Color = MaterialTheme.colorScheme.onSurface

public fun DrawScope.drawLevelSilhouette(
    solution: List<String>,
    boardOrigin: Offset,
    cellSize: Float,
    color: Color,
) {
    solution.forEachIndexed { rowIndex, row ->
        var columnIndex = 0

        while (columnIndex < row.length) {
            if (row[columnIndex] != '1') {
                columnIndex += 1
                continue
            }

            val runStart = columnIndex
            while (columnIndex < row.length && row[columnIndex] == '1') {
                columnIndex += 1
            }

            drawRect(
                color = color,
                topLeft =
                    Offset(
                        x = boardOrigin.x + (runStart * cellSize),
                        y = boardOrigin.y + (rowIndex * cellSize),
                    ),
                size = Size(width = (columnIndex - runStart) * cellSize, height = cellSize),
            )
        }
    }
}
