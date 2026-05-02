@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package dev.stekl0.nonokt.feature.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stekl0.nonokt.core.designsystem.icon.ArrowBack
import dev.stekl0.nonokt.core.model.GameLevel
import dev.stekl0.nonokt.feature.game.ui.GameFailedDialog
import dev.stekl0.nonokt.feature.game.ui.GameSolvedDialog
import dev.stekl0.nonokt.feature.game.ui.NonogramBoard
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private val ScreenHorizontalPadding = 16.dp
private val ScreenVerticalPadding = 12.dp
private val SectionSpacing = 16.dp

@Composable
internal fun GameScreen(
    packId: String,
    level: GameLevel,
    onBackClick: () -> Unit,
    onLevelsClick: () -> Unit,
    onRestartClick: () -> Unit,
    onNextLevelClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = koinViewModel(parameters = { parametersOf(packId, level) }),
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
                            contentDescription = stringResource(R.string.feature_game_back),
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
                actionsEnabled = !state.isCompletionPersistenceBlocking,
                completionPersistenceState = state.completionPersistenceState,
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
