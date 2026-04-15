package dev.stekl0.nonokt.feature.game.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe
import pro.respawn.flowmvi.compose.preview.EmptyReceiver

@Composable
internal fun GameScreen(viewModel: GameViewModel) =
    with(viewModel.store) {
        val state by subscribe { action ->
            when (action) {
                else -> TODO()
            }
        }

        GameScreenContent(state)
    }

@Composable
private fun IntentReceiver<GameIntent>.GameScreenContent(state: GameState) {
    when (state) {
        //
        else -> TODO()
    }
}

@Composable
@Preview
private fun GameScreenPreview() =
    EmptyReceiver {
        GameScreenContent(TODO())
    }
