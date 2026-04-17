package dev.stekl0.nonokt.feature.game.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.stekl0.nonokt.feature.game.api.GameLevel
import dev.stekl0.nonokt.feature.game.api.GameNavKey
import dev.stekl0.nonokt.feature.game.impl.GameScreen

public fun EntryProviderScope<NavKey>.gameEntry(
    onBackClick: () -> Unit,
    onLevelsClick: () -> Unit,
    onNextLevelClick: (GameLevel, List<GameLevel>) -> Unit,
) {
    entry<GameNavKey> { key ->
        val nextLevel = key.remainingLevels.firstOrNull()
        GameScreen(
            level = key.level,
            onBackClick = onBackClick,
            onLevelsClick = onLevelsClick,
            onNextLevelClick =
                if (nextLevel == null) {
                    null
                } else {
                    {
                        onNextLevelClick(
                            nextLevel,
                            key.remainingLevels.drop(1),
                        )
                    }
                },
        )
    }
}
