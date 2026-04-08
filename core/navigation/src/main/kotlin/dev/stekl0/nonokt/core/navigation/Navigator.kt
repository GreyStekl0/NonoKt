package dev.stekl0.nonokt.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey

public class Navigator(
    public val state: NavigationState,
) {
    /**
     * Navigate to a navigation key
     *
     * @param key - the navigation key to navigate to.
     */
    public fun navigate(key: NavKey) {
        state.backStack.add(key)
    }

    /**
     * Go back to the previous navigation key.
     */
    public fun goBack() {
        if (state.canGoBack) state.backStack.removeLastOrNull()
    }
}

@Composable
public fun rememberNavigator(state: NavigationState): Navigator =
    remember(state) { Navigator(state) }
