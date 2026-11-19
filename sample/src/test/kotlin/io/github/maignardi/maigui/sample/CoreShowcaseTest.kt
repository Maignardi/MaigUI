package io.github.maignardi.maigui.sample

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CoreShowcaseTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `sample app switches presets and theme mode in real time`() {
        composeRule.setContent {
            MaigSampleApp()
        }

        composeRule.onNodeWithText("MaigUI Design System").assertIsDisplayed()
        composeRule.onNodeWithText("Purple").performScrollTo().performClick()
        composeRule.onNodeWithText("Dark").performScrollTo().performClick()
        composeRule.onNodeWithText("Semantic Roles & WCAG AA Contrast")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
