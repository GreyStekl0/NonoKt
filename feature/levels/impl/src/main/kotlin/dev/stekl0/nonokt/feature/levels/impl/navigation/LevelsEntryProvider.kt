package dev.stekl0.nonokt.feature.levels.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.stekl0.nonokt.feature.levels.impl.LevelsScreen

public fun EntryProviderScope<NavKey>.levelsEntry(onLevelClick: (String, Int) -> Unit) {
    entry<LevelsNavKey> {
        LevelsScreen(onLevelClick = onLevelClick)
    }
}
