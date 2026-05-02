package dev.stekl0.nonokt.feature.game.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.stekl0.nonokt.core.model.GameLevel
import dev.stekl0.nonokt.feature.game.GameScreen

public fun EntryProviderScope<NavKey>.gameEntry(
    resolveLevels: (String) -> List<GameLevel>?,
    onBackClick: () -> Unit,
    onLevelsClick: () -> Unit,
    onRestartLevelClick: (String, String) -> Unit,
    onNextLevelClick: (String, String) -> Unit,
) {
    entry<GameNavKey> { key ->
        val levelPack = resolveLevels(key.packId)
        val levelIndex =
            key.levelId
                ?.let { levelId -> levelPack?.indexOfFirst { level -> level.id == levelId } }
                ?: key.legacyLevelIndex
                ?: -1
        val level = levelPack?.getOrNull(levelIndex)
        if (levelPack == null || level == null) {
            LaunchedEffect(key) {
                onLevelsClick()
            }
            return@entry
        }

        val nextLevelIndex =
            (levelIndex + 1)
                .takeIf { index -> index < levelPack.size }
        val nextLevelId = nextLevelIndex?.let { index -> levelPack[index].id }
        GameScreen(
            packId = key.packId,
            level = level,
            onBackClick = onBackClick,
            onLevelsClick = onLevelsClick,
            onRestartClick = {
                onRestartLevelClick(
                    key.packId,
                    level.id,
                )
            },
            onNextLevelClick =
                if (nextLevelId == null) {
                    null
                } else {
                    {
                        onNextLevelClick(
                            key.packId,
                            nextLevelId,
                        )
                    }
                },
        )
    }
}
