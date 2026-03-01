package dev.stekl0.nonokt.core.navigation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/**
 * Create a navigation state that persists config changes and process death.
 */
public class NavigationState(
    public val startKey: NavKey,
    public val stack: NavBackStack<NavKey>,
) {
    public val currentKey: NavKey by derivedStateOf { stack.last() }
    public val canGoBack: Boolean by derivedStateOf { stack.size > 1 }
}
