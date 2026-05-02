package dev.stekl0.nonokt.feature.game.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.stekl0.nonokt.core.model.GameLevel
import dev.stekl0.nonokt.core.ui.draw.drawLevelSilhouette
import dev.stekl0.nonokt.feature.game.CompletionPersistenceState
import dev.stekl0.nonokt.feature.game.R

private val DialogMinHeight = 320.dp
private val PreviewPadding = 24.dp

@Composable
internal fun GameSolvedDialog(
    level: GameLevel,
    actionsEnabled: Boolean,
    completionPersistenceState: CompletionPersistenceState,
    onLevelsClick: () -> Unit,
    onNextLevelClick: (() -> Unit)?,
) {
    GameOutcomeDialogScaffold(
        title = R.string.feature_game_completion_title,
        testTag = "game_solved_dialog",
        minHeight = DialogMinHeight,
    ) {
        SolvedDialogPreview(level = level)

        CompletionPersistenceMessage(completionPersistenceState = completionPersistenceState)

        SolvedDialogActions(
            actionsEnabled = actionsEnabled,
            onLevelsClick = onLevelsClick,
            onNextLevelClick = onNextLevelClick,
        )
    }
}

@Composable
private fun CompletionPersistenceMessage(completionPersistenceState: CompletionPersistenceState) {
    val (message, color) =
        when (completionPersistenceState) {
            CompletionPersistenceState.SAVING -> {
                R.string.feature_game_completion_saving to MaterialTheme.colorScheme.onSurfaceVariant
            }

            CompletionPersistenceState.FAILED -> {
                R.string.feature_game_completion_save_failed to MaterialTheme.colorScheme.error
            }

            CompletionPersistenceState.NOT_STARTED,
            CompletionPersistenceState.SAVED,
            -> {
                return
            }
        }
    Text(
        text = stringResource(message),
        style = MaterialTheme.typography.bodyMedium,
        color = color,
    )
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
    actionsEnabled: Boolean,
    onLevelsClick: () -> Unit,
    onNextLevelClick: (() -> Unit)?,
) {
    if (onNextLevelClick != null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(GameOutcomeDialogActionSpacing),
        ) {
            OutlinedButton(
                onClick = onLevelsClick,
                enabled = actionsEnabled,
                modifier = Modifier.weight(1f).testTag("game_solved_levels"),
            ) {
                Text(text = stringResource(R.string.feature_game_completion_levels))
            }
            Button(
                onClick = onNextLevelClick,
                enabled = actionsEnabled,
                modifier = Modifier.weight(1f).testTag("game_solved_next"),
            ) {
                Text(text = stringResource(R.string.feature_game_completion_next))
            }
        }
    } else {
        Button(
            onClick = onLevelsClick,
            enabled = actionsEnabled,
            modifier = Modifier.fillMaxWidth().testTag("game_solved_levels"),
        ) {
            Text(text = stringResource(R.string.feature_game_completion_levels))
        }
    }
}

@Composable
private fun SolvedLevelPreview(
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
