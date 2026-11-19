package io.github.maignardi.maigui.sample

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
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
    fun `catalog navigates through tabs and updates theme controls`() {
        composeRule.setContent {
            MaigSampleApp()
        }

        composeRule.onNodeWithText("MaigUI Design System").assertIsDisplayed()
        composeRule.onAllNodesWithText("Purple").onFirst().performClick()
        composeRule.onNodeWithText("Dark").performScrollTo().performClick()
        composeRule.onNodeWithText("Semantic Roles & WCAG AA Contrast")
            .performScrollTo()
            .assertIsDisplayed()

        // Navigate to Buttons tab
        composeRule.onNodeWithText("Botões").performScrollTo().performClick()
        composeRule.onNodeWithText("Variantes (MaigButtonVariant)")
            .performScrollTo()
            .assertIsDisplayed()

        // Navigate to Forms via bottom navigation and trigger validation
        composeRule.onAllNodesWithText("Forms").onFirst().performScrollTo().performClick()
        composeRule.onNodeWithText("Validar & Enviar").performScrollTo().performClick()
        composeRule.onNodeWithText("Existem erros de validação no formulário.").assertIsDisplayed()
    }
}
