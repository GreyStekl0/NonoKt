package dev.stekl0.nonokt.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import dev.stekl0.nonokt.MainActivity
import org.junit.Rule
import org.junit.Test

class NonoktAppNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun opensGameFromLevelsAndReturnsBack() {
        composeRule.onNodeWithTag("level:small_1").assertExists().performClick()

        composeRule.onNodeWithTag("game:small_1").assertExists()

        composeRule.onNodeWithTag("game_back").assertExists().performClick()

        composeRule.onNodeWithTag("game:small_1").assertDoesNotExist()
        composeRule.onNodeWithTag("level:small_1").assertExists()
    }
}
