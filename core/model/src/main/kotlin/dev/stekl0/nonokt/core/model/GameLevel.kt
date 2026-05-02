package dev.stekl0.nonokt.core.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = GameLevelSerializer::class)
public class GameLevel(
    public val id: String,
    solution: List<String>,
) {
    public val solution: List<String> = solution.toList()

    init {
        require(id.isNotBlank()) {
            "GameLevel id must not be blank."
        }
        require('/' !in id) {
            "GameLevel id must not contain '/'."
        }
        require(solution.isNotEmpty()) {
            "GameLevel solution must not be empty."
        }
        require(solution.all { row -> row.length == solution.size }) {
            "GameLevel solution must be square."
        }
        require(solution.all { row -> row.all { cell -> cell == '0' || cell == '1' } }) {
            "GameLevel solution must contain only '0' and '1'."
        }
    }

    public val size: Int
        get() = solution.size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameLevel) return false

        return id == other.id && solution == other.solution
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + solution.hashCode()
        return result
    }

    override fun toString(): String = "GameLevel(id=$id, solution=$solution)"
}

public object GameLevelSerializer : KSerializer<GameLevel> {
    private val delegate = SerializableGameLevel.serializer()

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(
        encoder: Encoder,
        value: GameLevel,
    ) {
        delegate.serialize(
            encoder = encoder,
            value =
                SerializableGameLevel(
                    id = value.id,
                    solution = value.solution,
                ),
        )
    }

    override fun deserialize(decoder: Decoder): GameLevel {
        val value = delegate.deserialize(decoder)
        return GameLevel(
            id = value.id,
            solution = value.solution,
        )
    }
}

@Serializable
private data class SerializableGameLevel(
    val id: String,
    val solution: List<String>,
)
