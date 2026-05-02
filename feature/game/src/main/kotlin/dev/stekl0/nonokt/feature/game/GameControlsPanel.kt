package dev.stekl0.nonokt.feature.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.stekl0.nonokt.core.designsystem.icon.Close
import dev.stekl0.nonokt.core.designsystem.icon.Edit
import dev.stekl0.nonokt.core.designsystem.icon.Redo
import dev.stekl0.nonokt.core.designsystem.icon.Undo

@Composable
internal fun GameControlsPanel(
    state: GameState,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onModeChange: (GameMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 3.dp,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HistoryButtonsRow(
                canUndo = state.canUndo,
                canRedo = state.canRedo,
                onUndoClick = onUndoClick,
                onRedoClick = onRedoClick,
            )
            ModeSelector(
                selectedMode = state.mode,
                onModeChange = onModeChange,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ModeSelector(
    selectedMode: GameMode,
    onModeChange: (GameMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
    ) {
        GameMode.entries.forEachIndexed { index, mode ->
            val selected = selectedMode == mode
            SegmentedButton(
                selected = selected,
                onClick = {
                    if (!selected) onModeChange(mode)
                },
                modifier = Modifier.weight(1f),
                shape =
                    SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = GameMode.entries.size,
                    ),
            ) {
                ModeSegmentContent(mode = mode)
            }
        }
    }
}

@Composable
private fun ModeSegmentContent(mode: GameMode) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector =
                when (mode) {
                    GameMode.FILL -> Edit
                    GameMode.MARK -> Close
                },
            contentDescription = null,
        )
        Text(text = mode.label())
    }
}

@Composable
private fun HistoryButtonsRow(
    canUndo: Boolean,
    canRedo: Boolean,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedIconButton(
            onClick = onUndoClick,
            enabled = canUndo,
        ) {
            Icon(
                imageVector = Undo,
                contentDescription = stringResource(R.string.feature_game_undo),
            )
        }

        OutlinedIconButton(
            onClick = onRedoClick,
            enabled = canRedo,
        ) {
            Icon(
                imageVector = Redo,
                contentDescription = stringResource(R.string.feature_game_redo),
            )
        }
    }
}
