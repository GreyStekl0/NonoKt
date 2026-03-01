package dev.stekl0.nonokt.core.navigation

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
        state.stack.remove(key) // singleTop-like
        state.stack.add(key)
    }

    /**
     * Go back to the previous navigation key.
     */
    public fun goBack() {
        if (state.canGoBack) state.stack.removeLastOrNull()
    }
}
