package dev.stekl0.nonokt.feature.levels.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.stekl0.nonokt.core.navigation.Navigator
import dev.stekl0.nonokt.feature.levels.api.LevelsNavKey
import dev.stekl0.nonokt.feature.levels.impl.LevelsScreen

public fun EntryProviderScope<NavKey>.levelsEntry(navigator: Navigator) {
    entry<LevelsNavKey> {
        LevelsScreen()
    }
}
