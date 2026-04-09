package dev.stekl0.nonokt.feature.levels.impl

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.dsl.LambdaIntent

internal enum class Tab {
    SMALL,
    MEDIUM,
    LARGE,
    ;

    val assetPath: String
        get() =
            when (this) {
                SMALL -> "levels/small.json"
                MEDIUM -> "levels/medium.json"
                LARGE -> "levels/large.json"
            }
}

internal sealed interface LevelsState : MVIState {
    @Immutable
    data class Content(
        val selectedTab: Tab = Tab.SMALL,
        val levelPacks: ImmutableMap<Tab, LevelPack> = persistentMapOf(),
    ) : LevelsState {
        val levels: ImmutableList<Level>
            get() = levelPacks[selectedTab]?.levels ?: persistentListOf()
    }

    data object Loading : LevelsState

    data class Error(
        val cause: Exception?,
    ) : LevelsState
}

internal typealias LevelsIntent = LambdaIntent<LevelsState, LevelsAction>

internal sealed interface LevelsAction : MVIAction

@Immutable
internal data class LevelPack(
    val levels: ImmutableList<Level>,
) {
    internal constructor(levels: List<Level>) : this(levels.toImmutableList())
}

@Immutable
internal data class Level(
    val id: String,
    val solution: ImmutableList<String>,
) {
    internal constructor(id: String, solution: List<String>) : this(id, solution.toImmutableList())

    val width: Int
        get() = solution.first().length

    val height: Int
        get() = solution.size
}

@Serializable
internal data class LevelPackPayload(
    val levels: List<LevelPayload>,
)

@Serializable
internal data class LevelPayload(
    val id: String,
    val solution: List<String>,
)

internal fun LevelPackPayload.toModel(): LevelPack = LevelPack(levels = levels.map(LevelPayload::toModel))

internal fun LevelPayload.toModel(): Level =
    Level(
        id = id,
        solution = solution,
    )
