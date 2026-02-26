package dev.stekl0.nonokt.feature.levels.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe
import pro.respawn.flowmvi.compose.preview.EmptyReceiver

@Composable
internal fun LevelsScreen(viewModel: LevelsViewModel) =
    with(viewModel.store) {
        val state by subscribe { action ->
            when (action) {
                else -> TODO()
            }
        }

        LevelsScreenContent(state)
    }

@Composable
private fun IntentReceiver<LevelsIntent>.LevelsScreenContent(state: LevelsState) {
    when (state) {
        //
        else -> TODO()
    }
}

@Composable
@Preview
private fun LevelsScreenPreview() =
    EmptyReceiver {
        LevelsScreenContent(TODO())
    }
