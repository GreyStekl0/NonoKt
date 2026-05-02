package dev.stekl0.nonokt.feature.levels.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.stekl0.nonokt.core.data.levelCompletionId
import dev.stekl0.nonokt.core.designsystem.icon.QuestionMark
import dev.stekl0.nonokt.core.model.GameLevel
import dev.stekl0.nonokt.core.ui.draw.drawLevelSilhouette
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

private val LevelGridSpacing: Dp = 12.dp
private val LevelGridPadding: PaddingValues = PaddingValues(16.dp)
private val LevelTileBorderWidth: Dp = 2.dp
private val LevelTileContentPadding: Dp = 4.dp
private val HiddenLevelIconSize: Dp = 36.dp

private object LevelsGridLayout {
    const val COLUMN_COUNT: Int = 4
}

@Composable
internal fun LevelsGrid(
    packId: String,
    levels: ImmutableList<GameLevel>,
    completedLevelIds: ImmutableSet<String>,
    onLevelClick: (String, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(LevelsGridLayout.COLUMN_COUNT),
        modifier = modifier,
        contentPadding = LevelGridPadding,
        horizontalArrangement = Arrangement.spacedBy(LevelGridSpacing),
        verticalArrangement = Arrangement.spacedBy(LevelGridSpacing),
    ) {
        itemsIndexed(
            items = levels,
            key = { _, level -> level.id },
        ) { index, level ->
            LevelTile(
                level = level,
                isCompleted =
                    levelCompletionId(
                        packId = packId,
                        levelId = level.id,
                    ) in completedLevelIds,
                onClick = {
                    onLevelClick(
                        packId,
                        index,
                    )
                },
            )
        }
    }
}

@Composable
private fun LevelTile(
    level: GameLevel,
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor =
        if (isCompleted) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.72f)
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }

    OutlinedCard(
        modifier =
            modifier
                .aspectRatio(1f)
                .testTag("level:${level.id}"),
        onClick = onClick,
        border =
            BorderStroke(
                width = LevelTileBorderWidth,
                color = borderColor,
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
            if (isCompleted) {
                LevelThumbnail(
                    level = level,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                HiddenLevelMark()
            }
        }
    }
}

@Composable
private fun HiddenLevelMark() {
    Icon(
        imageVector = QuestionMark,
        contentDescription = null,
        modifier = Modifier.size(HiddenLevelIconSize),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun LevelThumbnail(
    level: GameLevel,
    modifier: Modifier = Modifier,
) {
    val foregroundColor = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = modifier) {
        val boardSize = level.size

        val cellSize = minOf(size.width / boardSize, size.height / boardSize)
        val boardDimension = boardSize * cellSize
        val boardOrigin =
            Offset(
                x = (size.width - boardDimension) / 2f,
                y = (size.height - boardDimension) / 2f,
            )

        drawLevelSilhouette(
            solution = level.solution,
            boardOrigin = boardOrigin,
            cellSize = cellSize,
            color = foregroundColor,
        )
    }
}
