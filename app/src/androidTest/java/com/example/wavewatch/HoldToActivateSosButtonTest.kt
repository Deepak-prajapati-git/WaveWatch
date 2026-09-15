package com.example.wavewatch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wavewatch.ui.components.HoldToActivateSosButton
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HoldToActivateSosButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHoldToActivateButtonRenders() {
        composeTestRule.setContent {
            HoldToActivateSosButton(
                onSosTriggered = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Hold to activate SOS emergency beacon. Press and hold for 3 seconds.")
            .assertIsDisplayed()
    }
}
