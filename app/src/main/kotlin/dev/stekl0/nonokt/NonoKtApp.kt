package dev.stekl0.nonokt

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.stekl0.nonokt.navigation.NonoKtNavHost

@Composable
internal fun NonoKtApp(modifier: Modifier = Modifier) {
    val activity = LocalActivity.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        NonoKtNavHost(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            onBackAtRoot = { activity?.finish() },
        )
    }
}
