package dev.stekl0.nonokt.feature.levels.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.stekl0.nonokt.feature.levels.LevelsScreen

public fun EntryProviderScope<NavKey>.levelsEntry(onLevelClick: (String, String) -> Unit) {
    entry<LevelsNavKey> {
        LevelsScreen(onLevelClick = onLevelClick)
    }
}
