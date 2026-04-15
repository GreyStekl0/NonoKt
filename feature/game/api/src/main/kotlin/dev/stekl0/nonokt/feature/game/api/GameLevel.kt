package dev.stekl0.nonokt.feature.game.api

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import dev.stekl0.nonokt.core.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
@Immutable
public data class GameLevel(
    val id: String,
    val solution: List<String>,
) {
    public val width: Int
        get() = solution.first().length

    public val height: Int
        get() = solution.size
}

@Serializable
public data class GameNavKey(
    val level: GameLevel,
) : NavKey

public fun Navigator.navigateToGame(level: GameLevel) {
    navigate(GameNavKey(level))
}
