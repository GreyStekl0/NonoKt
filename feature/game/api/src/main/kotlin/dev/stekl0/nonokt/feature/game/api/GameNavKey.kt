package dev.stekl0.nonokt.feature.game.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
public data class GameNavKey(
    val level: GameLevel,
    val remainingLevels: List<GameLevel> = emptyList(),
    val instanceId: String = "",
) : NavKey
