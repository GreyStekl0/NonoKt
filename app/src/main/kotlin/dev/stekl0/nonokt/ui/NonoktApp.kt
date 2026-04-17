package dev.stekl0.nonokt.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.stekl0.nonokt.core.navigation.rememberNavigationState
import dev.stekl0.nonokt.core.navigation.rememberNavigator
import dev.stekl0.nonokt.feature.game.api.GameNavKey
import dev.stekl0.nonokt.feature.game.impl.navigation.gameEntry
import dev.stekl0.nonokt.feature.levels.api.LevelsNavKey
import dev.stekl0.nonokt.feature.levels.impl.navigation.levelsEntry

@Composable
public fun NonoktApp(modifier: Modifier = Modifier) {
    val navigationState = rememberNavigationState(startKey = LevelsNavKey)
    val navigator = rememberNavigator(state = navigationState)
    val entryProvider =
        remember(navigator) {
            entryProvider {
                levelsEntry(
                    onLevelClick = { level, remainingLevels ->
                        navigator.navigate(
                            GameNavKey(
                                level = level,
                                remainingLevels = remainingLevels,
                            ),
                        )
                    },
                )
                gameEntry(
                    onBackClick = navigator::goBack,
                    onLevelsClick = navigator::goBack,
                    onNextLevelClick = { nextLevel, remainingLevels ->
                        navigator.goBack()
                        navigator.navigate(
                            GameNavKey(
                                level = nextLevel,
                                remainingLevels = remainingLevels,
                            ),
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
