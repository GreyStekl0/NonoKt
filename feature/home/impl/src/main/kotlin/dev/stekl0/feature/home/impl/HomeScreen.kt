package dev.stekl0.feature.home.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe
import pro.respawn.flowmvi.compose.preview.EmptyReceiver

@Composable
internal fun HomeScreen(viewModel: HomeViewModel) =
    with(viewModel.store) {
        val state by subscribe { action ->
            when (action) {
                else -> TODO()
            }
        }

        HomeScreenContent(state)
    }

@Composable
private fun IntentReceiver<HomeIntent>.HomeScreenContent(state: HomeState) {
    when (state) {
        //
        else -> TODO()
    }
}

@Composable
@Preview
private fun HomeScreenPreview() =
    EmptyReceiver {
        HomeScreenContent(TODO())
    }
