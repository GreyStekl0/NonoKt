package dev.stekl0.nonokt.feature.game.impl.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
public data class GameNavKey(
    val packId: String,
    val levelIndex: Int,
    val instanceId: String = "",
) : NavKey
