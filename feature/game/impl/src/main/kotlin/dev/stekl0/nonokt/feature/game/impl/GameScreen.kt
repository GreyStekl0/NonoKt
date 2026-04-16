@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package dev.stekl0.nonokt.feature.game.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stekl0.nonokt.core.designsystem.icon.ArrowBack
import dev.stekl0.nonokt.feature.game.api.GameLevel
import dev.stekl0.nonokt.feature.game.impl.GameLimits.MAX_ERROR_COUNT
import dev.stekl0.nonokt.feature.game.impl.ui.NonogramBoard
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pro.respawn.flowmvi.compose.dsl.subscribe

private val ScreenHorizontalPadding = 16.dp
private val ScreenVerticalPadding = 12.dp
private val SectionSpacing = 16.dp

@Composable
internal fun GameScreen(
    level: GameLevel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = koinViewModel(parameters = { parametersOf(level) }),
) {
    val state by viewModel.store.subscribe()

    GameContent(
        state = state,
        onBackClick = onBackClick,
        onCellPress = viewModel::onCellPressed,
        onUndoClick = viewModel::undo,
        onRedoClick = viewModel::redo,
        onModeChange = viewModel::onModeChanged,
        modifier = modifier,
    )
}

@Composable
private fun GameContent(
    state: GameState,
    onBackClick: () -> Unit,
    onCellPress: (Int, Int) -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onModeChange: (GameMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ArrowBack,
                            contentDescription = stringResource(R.string.game_back),
                        )
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(
                        horizontal = ScreenHorizontalPadding,
                        vertical = ScreenVerticalPadding,
                    ),
            verticalArrangement = Arrangement.spacedBy(SectionSpacing),
        ) {
            NonogramBoard(
                state = state,
                onCellPress = onCellPress,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            )

            GameSummaryPanel(
                state = state,
                modifier = Modifier.fillMaxWidth(),
            )

            GameControlsPanel(
                state = state,
                onUndoClick = onUndoClick,
                onRedoClick = onRedoClick,
                onModeChange = onModeChange,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun GameSummaryPanel(
    state: GameState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SummaryCard(
            title = R.string.game_errors_title,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = gameErrorsText(state),
                style = MaterialTheme.typography.titleMedium,
            )
            ErrorIndicators(errorCount = state.errorCount)
        }

        SummaryCard(
            title = R.string.game_size_title,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = gameSizeText(state),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = state.progressLabel(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.wrapContentHeight(),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            content()
        }
    }
}

@Composable
private fun ErrorIndicators(errorCount: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(MAX_ERROR_COUNT) { index ->
            Box(
                modifier =
                    Modifier
                        .size(10.dp)
                        .background(
                            color =
                                if (index < errorCount) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant
                                },
                            shape = CircleShape,
                        ),
            )
        }
    }
}

@Composable
private fun GameControlsPanel(
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
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onUndoClick,
                    enabled = state.canUndo,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(R.string.game_undo))
                }

                OutlinedButton(
                    onClick = onRedoClick,
                    enabled = state.canRedo,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(R.string.game_redo))
                }
            }

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                GameMode.entries.forEachIndexed { index, mode ->
                    SegmentedButton(
                        selected = state.mode == mode,
                        onClick = { onModeChange(mode) },
                        modifier = Modifier.weight(1f),
                        shape =
                            SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = GameMode.entries.size,
                            ),
                    ) {
                        Text(text = mode.label())
                    }
                }
            }

            Text(
                text = state.statusText(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private val PreviewLevel =
    GameLevel(
        id = "preview_6x6",
        solution =
            listOf(
                "010000",
                "001000",
                "011100",
                "111110",
                "011100",
                "001000",
            ),
    )

private val PreviewState =
    GameState
        .create(PreviewLevel)
        .onCellPressed(row = 0, column = 1)
        .withMode(GameMode.MARK)
        .onCellPressed(row = 1, column = 1)
        .withMode(GameMode.FILL)
        .onCellPressed(row = 0, column = 0)

@Preview(showBackground = true, heightDp = 900, widthDp = 420)
@Composable
private fun GameScreenPreview() {
    GameContent(
        state = PreviewState,
        onBackClick = {},
        onCellPress = { _, _ -> },
        onUndoClick = {},
        onRedoClick = {},
        onModeChange = {},
    )
}
