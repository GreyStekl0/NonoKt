package dev.stekl0.nonokt.feature.game.impl

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
public data class GameLevel(
    val id: String,
    val solution: List<String>,
) {
    public val size: Int
        get() = solution.size
}
