package dev.stekl0.nonokt.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.stekl0.nonokt.core.navigation.Navigator
import dev.stekl0.nonokt.core.navigation.rememberNavigationState
import dev.stekl0.nonokt.core.navigation.rememberNavigator
import dev.stekl0.nonokt.feature.game.impl.navigation.GameNavKey
import dev.stekl0.nonokt.feature.game.impl.navigation.gameEntry
import dev.stekl0.nonokt.feature.levels.impl.LevelCompletionStore
import dev.stekl0.nonokt.feature.levels.impl.LevelPackSource
import dev.stekl0.nonokt.feature.levels.impl.navigation.LevelsNavKey
import dev.stekl0.nonokt.feature.levels.impl.navigation.levelsEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import timber.log.Timber
import java.util.UUID

@Composable
public fun NonoktApp(modifier: Modifier = Modifier) {
    val navigationState = rememberNavigationState(startKey = LevelsNavKey)
    val navigator = rememberNavigator(state = navigationState)
    val levelPackSource = koinInject<LevelPackSource>()
    val levelCompletionStore = koinInject<LevelCompletionStore>()
    val coroutineScope = rememberCoroutineScope()
    val entryProvider =
        remember(navigator, levelPackSource, levelCompletionStore) {
            entryProvider {
                levelsEntry(
                    onLevelClick = { packId, levelIndex ->
                        navigator.navigateToGameLevel(
                            packId = packId,
                            levelIndex = levelIndex,
                        )
                    },
                )
                gameEntry(
                    resolveLevels = levelPackSource::loadLevels,
                    onBackClick = navigator::goBack,
                    onLevelsClick = navigator::goBack,
                    onRestartLevelClick = { packId, levelIndex ->
                        navigator.restartGameLevel(
                            packId = packId,
                            levelIndex = levelIndex,
                        )
                    },
                    onNextLevelClick = { packId, levelIndex ->
                        navigator.goBack()
                        navigator.navigateToGameLevel(
                            packId = packId,
                            levelIndex = levelIndex,
                        )
                    },
                    onLevelComplete = { packId, levelId ->
                        coroutineScope.markLevelCompleted(
                            levelCompletionStore = levelCompletionStore,
                            packId = packId,
                            levelId = levelId,
                        )
                    },
                )
            }
        }

    NavDisplay(
        backStack = navigationState.backStack,
        modifier = modifier.fillMaxSize(),
        onBack = navigator::goBack,
        entryDecorators =
            listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        entryProvider = entryProvider,
    )
}

private fun CoroutineScope.markLevelCompleted(
    levelCompletionStore: LevelCompletionStore,
    packId: String,
    levelId: String,
) {
    launch {
        runCatching {
            levelCompletionStore.markCompleted(
                packId = packId,
                levelId = levelId,
            )
        }.onFailure { throwable ->
            Timber.e(throwable, "Failed to persist level completion")
        }
    }
}

private fun Navigator.restartGameLevel(
    packId: String,
    levelIndex: Int,
) {
    goBack()
    navigateToGameLevel(
        packId = packId,
        levelIndex = levelIndex,
        instanceId = UUID.randomUUID().toString(),
    )
}

private fun Navigator.navigateToGameLevel(
    packId: String,
    levelIndex: Int,
    instanceId: String = "",
) {
    navigate(
        GameNavKey(
            packId = packId,
            levelIndex = levelIndex,
            instanceId = instanceId,
        ),
    )
}
