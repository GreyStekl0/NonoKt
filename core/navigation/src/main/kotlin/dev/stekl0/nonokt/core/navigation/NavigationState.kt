package dev.stekl0.nonokt.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

/**
 * Holds the single back stack used by the app shell.
 */
public class NavigationState internal constructor(
    public val startKey: NavKey,
    internal val mutableBackStack: NavBackStack<NavKey>,
) {
    public val backStack: List<NavKey>
        get() = mutableBackStack

    public val currentKey: NavKey
        get() = mutableBackStack.lastOrNull() ?: startKey

    public val canGoBack: Boolean
        get() = mutableBackStack.size > 1
}

/**
 * Creates a Navigation 3 back stack that survives process death and configuration changes.
 */
@Composable
public fun rememberNavigationState(startKey: NavKey): NavigationState {
    val backStack = rememberNavBackStack(startKey)
    return remember(startKey, backStack) {
        NavigationState(
            startKey = startKey,
            mutableBackStack = backStack,
        )
    }
}
