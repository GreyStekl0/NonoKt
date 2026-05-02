package dev.stekl0.nonokt.feature.levels

import androidx.compose.runtime.Immutable
import dev.stekl0.nonokt.core.data.LevelPack
import dev.stekl0.nonokt.core.data.LevelPackIds
import dev.stekl0.nonokt.core.model.GameLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf

internal enum class Tab(
    val packId: String,
) {
    SMALL(LevelPackIds.SMALL),
    MEDIUM(LevelPackIds.MEDIUM),
    LARGE(LevelPackIds.LARGE),
}

@Immutable
internal data class LevelsState(
    val selectedTab: Tab = Tab.SMALL,
    val levelPacks: ImmutableMap<String, LevelPack> = persistentMapOf(),
    val completedLevelIds: ImmutableSet<String> = persistentSetOf(),
) {
    val levels: ImmutableList<GameLevel>
        get() = levelPacks[selectedTab.packId]?.levels ?: persistentListOf()
}
