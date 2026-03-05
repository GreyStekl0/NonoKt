package dev.stekl0.nonokt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.rememberNavBackStack
import dev.stekl0.nonokt.core.navigation.NavigationState
import dev.stekl0.nonokt.feature.levels.api.LevelsNavKey

@Composable
internal fun rememberNonoKtNavigationState(): NavigationState {
    val backStack = rememberNavBackStack(LevelsNavKey)
    return remember(backStack) {
        NavigationState(
            startKey = LevelsNavKey,
            stack = backStack,
        )
    }
}
