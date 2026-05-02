package dev.stekl0.nonokt.core.model

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
