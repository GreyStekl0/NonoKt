package dev.stekl0.nonokt.feature.game.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.stekl0.nonokt.feature.game.api.GameNavKey
import dev.stekl0.nonokt.feature.game.impl.GameRoute

public fun EntryProviderScope<NavKey>.gameEntry(onBackClick: () -> Unit) {
    entry<GameNavKey> { key ->
        GameRoute(
            level = key.level,
            onBackClick = onBackClick,
        )
    }
}
