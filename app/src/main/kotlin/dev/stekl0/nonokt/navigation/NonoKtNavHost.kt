package dev.stekl0.nonokt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.stekl0.nonokt.core.navigation.Navigator
import dev.stekl0.nonokt.feature.levels.impl.navigation.levelsEntry

@Composable
internal fun NonoKtNavHost(
    modifier: Modifier = Modifier,
    onBackAtRoot: () -> Unit = {},
) {
    val navigationState = rememberNonoKtNavigationState()
    val navigator =
        remember(navigationState) {
            Navigator(navigationState)
        }
    val entries =
        entryProvider {
            levelsEntry(navigator)
        }

    NavDisplay(
        backStack = navigationState.stack,
        entryProvider = entries,
        modifier = modifier,
        onBack = {
            if (navigationState.canGoBack) {
                navigator.goBack()
            } else {
                onBackAtRoot()
            }
        },
    )
}
