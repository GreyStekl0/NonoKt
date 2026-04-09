package dev.stekl0.nonokt.feature.levels.impl

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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

@Immutable
internal data class LevelsState(
    val selectedTab: Tab = Tab.SMALL,
    val levelPacks: ImmutableMap<Tab, LevelPack> = persistentMapOf(),
) : MVIState {
    val levels: ImmutableList<Level>
        get() = levelPacks[selectedTab]?.levels ?: persistentListOf()
}

internal typealias LevelsIntent = LambdaIntent<LevelsState, Nothing>

@Serializable
@Immutable
internal data class LevelPack(
    @Serializable(with = LevelImmutableListSerializer::class)
    val levels: ImmutableList<Level>,
) {
    internal constructor(levels: List<Level>) : this(levels.toImmutableList())
}

@Serializable
@Immutable
internal data class Level(
    val id: String,
    @Serializable(with = StringImmutableListSerializer::class)
    val solution: ImmutableList<String>,
) {
    internal constructor(id: String, solution: List<String>) : this(id, solution.toImmutableList())

    val width: Int
        get() = solution.first().length

    val height: Int
        get() = solution.size
}

private class ImmutableListSerializer<T>(
    elementSerializer: KSerializer<T>,
) : KSerializer<ImmutableList<T>> {
    private val delegate = ListSerializer(elementSerializer)

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(
        encoder: Encoder,
        value: ImmutableList<T>,
    ) {
        delegate.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): ImmutableList<T> = delegate.deserialize(decoder).toImmutableList()
}

private object LevelImmutableListSerializer :
    KSerializer<ImmutableList<Level>> by ImmutableListSerializer(Level.serializer())

private object StringImmutableListSerializer :
    KSerializer<ImmutableList<String>> by ImmutableListSerializer(String.serializer())
