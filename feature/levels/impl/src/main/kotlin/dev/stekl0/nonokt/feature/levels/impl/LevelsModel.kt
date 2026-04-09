package dev.stekl0.nonokt.feature.levels.impl

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
    data class Content(
        val selectedTab: Tab = Tab.SMALL,
        val levelPacks: Map<Tab, LevelPack> = emptyMap(),
    ) : LevelsState {
        val levels: List<Level>
            get() = levelPacks.getValue(selectedTab).levels
    }

    data object Loading : LevelsState

    data class Error(
        val cause: Exception?,
    ) : LevelsState
}

internal typealias LevelsIntent = LambdaIntent<LevelsState, LevelsAction>

internal sealed interface LevelsAction : MVIAction

@Serializable
internal data class LevelPack(
    val levels: List<Level>,
)

@Serializable
internal data class Level(
    val id: String,
    val solution: List<String>,
) {
    val width: Int
        get() = solution.first().length

    val height: Int
        get() = solution.size
}
