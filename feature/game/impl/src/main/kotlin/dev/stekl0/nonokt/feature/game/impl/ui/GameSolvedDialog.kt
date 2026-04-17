package dev.stekl0.nonokt.feature.game.impl.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.stekl0.nonokt.core.designsystem.draw.drawLevelSilhouette
import dev.stekl0.nonokt.core.designsystem.draw.levelSilhouetteColor
import dev.stekl0.nonokt.feature.game.api.GameLevel
import dev.stekl0.nonokt.feature.game.impl.R

private val DialogMinHeight = 320.dp
private val DialogPadding = 20.dp
private val DialogSpacing = 16.dp
private val DialogActionSpacing = 12.dp
private val PreviewPadding = 24.dp

@Composable
internal fun GameSolvedDialog(
    level: GameLevel,
    hasNextLevel: Boolean,
    onLevelsClick: () -> Unit,
    onNextLevelClick: (() -> Unit)?,
) {
    Dialog(
        onDismissRequest = {},
        properties =
            DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
    ) {
        GameSolvedDialogContainer(
            level = level,
            hasNextLevel = hasNextLevel,
            onLevelsClick = onLevelsClick,
            onNextLevelClick = onNextLevelClick,
        )
    }
}

@Composable
private fun GameSolvedDialogContainer(
    level: GameLevel,
    hasNextLevel: Boolean,
    onLevelsClick: () -> Unit,
    onNextLevelClick: (() -> Unit)?,
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 6.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = DialogMinHeight),
    ) {
        Column(
            modifier = Modifier.padding(DialogPadding),
            verticalArrangement = Arrangement.spacedBy(DialogSpacing),
        ) {
            Text(
                text = stringResource(R.string.game_completion_title),
                style = MaterialTheme.typography.titleLarge,
            )

            SolvedDialogPreview(level = level)

            SolvedDialogActions(
                hasNextLevel = hasNextLevel,
                onLevelsClick = onLevelsClick,
                onNextLevelClick = onNextLevelClick,
            )
        }
    }
}

@Composable
private fun SolvedDialogPreview(
    level: GameLevel,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) {
        SolvedLevelPreview(
            level = level,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(PreviewPadding),
        )
    }
}

@Composable
private fun SolvedDialogActions(
    hasNextLevel: Boolean,
    onLevelsClick: () -> Unit,
    onNextLevelClick: (() -> Unit)?,
) {
    if (hasNextLevel && onNextLevelClick != null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DialogActionSpacing),
        ) {
            OutlinedButton(
                onClick = onLevelsClick,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.game_completion_levels))
            }
            Button(
                onClick = onNextLevelClick,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.game_completion_next))
            }
        }
    } else {
        Button(
            onClick = onLevelsClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.game_completion_levels))
        }
    }
}

@Composable
private fun SolvedLevelPreview(
    level: GameLevel,
    modifier: Modifier = Modifier,
) {
    val foregroundColor = levelSilhouetteColor()

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
