package dev.stekl0.nonokt.feature.game.impl.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.stekl0.nonokt.feature.game.impl.GameLevel
import dev.stekl0.nonokt.feature.game.impl.GameScreen

public fun EntryProviderScope<NavKey>.gameEntry(
    resolveLevels: (String) -> List<GameLevel>?,
    onBackClick: () -> Unit,
    onLevelsClick: () -> Unit,
    onRestartLevelClick: (String, Int) -> Unit,
    onNextLevelClick: (String, Int) -> Unit,
) {
    entry<GameNavKey> { key ->
        val levelPack = resolveLevels(key.packId)
        val level = levelPack?.getOrNull(key.levelIndex)
        if (level == null) {
            LaunchedEffect(key) {
                onLevelsClick()
            }
            return@entry
        }

        val nextLevelIndex =
            (key.levelIndex + 1)
                .takeIf { index -> index < levelPack.size }
        GameScreen(
            level = level,
            onBackClick = onBackClick,
            onLevelsClick = onLevelsClick,
            onRestartClick = {
                onRestartLevelClick(
                    key.packId,
                    key.levelIndex,
                )
            },
            onNextLevelClick =
                if (nextLevelIndex == null) {
                    null
                } else {
                    {
                        onNextLevelClick(
                            key.packId,
                            nextLevelIndex,
                        )
                    }
                },
        )
    }
}
