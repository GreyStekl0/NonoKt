package dev.stekl0.nonokt.feature.game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

internal val GameOutcomeDialogPadding: Dp = 20.dp
internal val GameOutcomeDialogSpacing: Dp = 16.dp
internal val GameOutcomeDialogActionSpacing: Dp = 12.dp

@Composable
internal fun GameOutcomeDialogScaffold(
    title: Int,
    testTag: String,
    minHeight: Dp,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = {},
        properties =
            DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier =
                Modifier
                    .testTag(testTag)
                    .fillMaxWidth()
                    .heightIn(min = minHeight),
        ) {
            Column(
                modifier = Modifier.padding(GameOutcomeDialogPadding),
                verticalArrangement = Arrangement.spacedBy(GameOutcomeDialogSpacing),
            ) {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleLarge,
                )

                content()
            }
        }
    }
}
