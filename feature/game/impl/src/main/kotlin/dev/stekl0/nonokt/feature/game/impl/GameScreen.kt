@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package dev.stekl0.nonokt.feature.game.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stekl0.nonokt.core.designsystem.icon.ArrowBack
import dev.stekl0.nonokt.core.designsystem.icon.Close
import dev.stekl0.nonokt.core.designsystem.icon.Edit
import dev.stekl0.nonokt.core.designsystem.icon.Redo
import dev.stekl0.nonokt.core.designsystem.icon.Undo
import dev.stekl0.nonokt.feature.game.impl.GameLimits.MAX_ERROR_COUNT
import dev.stekl0.nonokt.feature.game.impl.ui.GameFailedDialog
import dev.stekl0.nonokt.feature.game.impl.ui.GameSolvedDialog
import dev.stekl0.nonokt.feature.game.impl.ui.NonogramBoard
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private val ScreenHorizontalPadding = 16.dp
private val ScreenVerticalPadding = 12.dp
private val SectionSpacing = 16.dp

@Composable
internal fun GameScreen(
    level: GameLevel,
    onBackClick: () -> Unit,
    onLevelsClick: () -> Unit,
    onRestartClick: () -> Unit,
    onNextLevelClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = koinViewModel(parameters = { parametersOf(level) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GameContent(
        state = state,
        onBackClick = onBackClick,
        onLevelsClick = onLevelsClick,
        onRestartClick = onRestartClick,
        onNextLevelClick = onNextLevelClick,
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
    onLevelsClick: () -> Unit,
    onRestartClick: () -> Unit,
    onNextLevelClick: (() -> Unit)?,
    onCellPress: (Int, Int) -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onModeChange: (GameMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.testTag("game:${state.level.id}"),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("game_back"),
                    ) {
                        Icon(
                            imageVector = ArrowBack,
                            contentDescription = stringResource(R.string.feature_game_impl_game_back),
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
                    .consumeWindowInsets(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = ScreenHorizontalPadding,
                        vertical = ScreenVerticalPadding,
                    ),
            verticalArrangement = Arrangement.spacedBy(SectionSpacing),
        ) {
            NonogramBoard(
                state = state,
                onCellPress = onCellPress,
                modifier = Modifier.fillMaxWidth(),
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

        GameOutcomeDialog(
            state = state,
            onLevelsClick = onLevelsClick,
            onRestartClick = onRestartClick,
            onNextLevelClick = onNextLevelClick,
        )
    }
}

@Composable
private fun GameOutcomeDialog(
    state: GameState,
    onLevelsClick: () -> Unit,
    onRestartClick: () -> Unit,
    onNextLevelClick: (() -> Unit)?,
) {
    when {
        state.isSolved -> {
            GameSolvedDialog(
                level = state.level,
                hasNextLevel = onNextLevelClick != null,
                onLevelsClick = onLevelsClick,
                onNextLevelClick = onNextLevelClick,
            )
        }

        state.isFailed -> {
            GameFailedDialog(
                onLevelsClick = onLevelsClick,
                onRestartClick = onRestartClick,
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
            title = R.string.feature_game_impl_game_errors_title,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = gameErrorsText(state),
                style = MaterialTheme.typography.titleMedium,
            )
            ErrorIndicators(errorCount = state.errorCount)
        }

        SummaryCard(
            title = R.string.feature_game_impl_game_size_title,
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
        Row(
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
            SegmentedButton(
                selected = selectedMode == mode,
                onClick = { onModeChange(mode) },
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
            imageVector = if (mode == GameMode.FILL) Edit else Close,
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
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedIconButton(
            onClick = onUndoClick,
            enabled = canUndo,
        ) {
            Icon(
                imageVector = Undo,
                contentDescription = stringResource(R.string.feature_game_impl_game_undo),
            )
        }

        OutlinedIconButton(
            onClick = onRedoClick,
            enabled = canRedo,
        ) {
            Icon(
                imageVector = Redo,
                contentDescription = stringResource(R.string.feature_game_impl_game_redo),
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
        onLevelsClick = {},
        onRestartClick = {},
        onNextLevelClick = null,
        onCellPress = { _, _ -> },
        onUndoClick = {},
        onRedoClick = {},
        onModeChange = {},
    )
}

@Preview(showBackground = true, heightDp = 900, widthDp = 300)
@Composable
private fun GameScreenCompactPreview() {
    GameContent(
        state = PreviewState,
        onBackClick = {},
        onLevelsClick = {},
        onRestartClick = {},
        onNextLevelClick = null,
        onCellPress = { _, _ -> },
        onUndoClick = {},
        onRedoClick = {},
        onModeChange = {},
    )
}
