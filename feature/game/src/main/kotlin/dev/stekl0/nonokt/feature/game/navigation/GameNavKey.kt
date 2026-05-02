package dev.stekl0.nonokt.feature.game.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GameNavKey(
    val packId: String,
    val levelId: String? = null,
    @SerialName("levelIndex")
    val legacyLevelIndex: Int? = null,
    val instanceId: String = "",
) : NavKey {
    init {
        require(levelId != null || legacyLevelIndex != null) {
            "GameNavKey must contain either levelId or legacy levelIndex."
        }
    }
}
