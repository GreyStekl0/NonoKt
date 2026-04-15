package dev.stekl0.nonokt.feature.levels.impl.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.stekl0.nonokt.feature.game.api.GameLevel
import kotlinx.collections.immutable.ImmutableList

private val LevelGridSpacing: Dp = 12.dp
private val LevelGridPadding: PaddingValues = PaddingValues(16.dp)
private val LevelTileBorderWidth: Dp = 2.dp
private val LevelTileContentPadding: Dp = 4.dp

private object LevelsGridLayout {
    const val COLUMN_COUNT: Int = 4
}

@Composable
internal fun LevelsGrid(
    levels: ImmutableList<GameLevel>,
    onLevelClick: (GameLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(LevelsGridLayout.COLUMN_COUNT),
        modifier = modifier,
        contentPadding = LevelGridPadding,
        horizontalArrangement = Arrangement.spacedBy(LevelGridSpacing),
        verticalArrangement = Arrangement.spacedBy(LevelGridSpacing),
    ) {
        items(
            items = levels,
            key = GameLevel::id,
        ) { level ->
            LevelTile(
                level = level,
                onClick = { onLevelClick(level) },
            )
        }
    }
}

@Composable
private fun LevelTile(
    level: GameLevel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier =
            modifier
                .aspectRatio(1f)
                .clickable(onClick = onClick),
        border =
            BorderStroke(
                width = LevelTileBorderWidth,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.72f),
            ),
        colors =
            CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(LevelTileContentPadding),
            contentAlignment = Alignment.Center,
        ) {
            LevelThumbnail(
                level = level,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun LevelThumbnail(
    level: GameLevel,
    modifier: Modifier = Modifier,
    foregroundColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Canvas(modifier = modifier) {
        val boardSize = level.size

        val cellSize = minOf(size.width / boardSize, size.height / boardSize)
        val boardDimension = boardSize * cellSize
        val boardOrigin =
            Offset(
                x = (size.width - boardDimension) / 2f,
                y = (size.height - boardDimension) / 2f,
            )

        level.solution.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, cell ->
                if (cell == '1') {
                    drawRect(
                        color = foregroundColor,
                        topLeft =
                            Offset(
                                x = boardOrigin.x + (columnIndex * cellSize),
                                y = boardOrigin.y + (rowIndex * cellSize),
                            ),
                        size = Size(width = cellSize, height = cellSize),
                    )
                }
            }
        }
    }
}
