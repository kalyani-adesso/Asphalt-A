package com.asphalt.resetpassword.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.resetpassword.viewmodel.ForgotPasswordViewModel
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class ForgotPasswordScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // Helper to create a test ViewModel without calling APIs
    private fun createTestViewModel(): ForgotPasswordViewModel {
        // We pass real instances, but we won't call network
        val authViewModel = AuthViewModel(authenticator = com.asphalt.android.repository.AuthenticatorImpl())
        val userRepo = UserRepository(apiService = com.asphalt.android.network.user.UserAPIServiceImpl(com.asphalt.android.network.KtorClient()))
        return ForgotPasswordViewModel(authViewModel, userRepo)
    }
    @Test
    fun emailField_and_button_displayed() {
        val vm = createTestViewModel()
        composeTestRule.setContent {
            ForgotPasswordScreen(onSendClick = {}, viewModel = vm)
        }
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("SEND RESET LINK").assertExists()
    }

    @Test
    fun forgot_password_isDisplayed(){
        val vm = createTestViewModel()
        composeTestRule.setContent {
            ForgotPasswordScreen(onSendClick = {}, viewModel = vm)
        }
        composeTestRule.onNodeWithText("Forgot Password?").assertExists()
        //composeTestRule.onNodeWithText("SEND RESET LINK").assertExists()
    }

    @Test
    fun emailPlaceHolderText_isDisplayed(){
            val vm = createTestViewModel()
            composeTestRule.setContent {
                ForgotPasswordScreen(onSendClick = {}, viewModel = vm)
            }
            composeTestRule.onNodeWithText("Enter your email").assertExists()
            //composeTestRule.onNodeWithText("SEND RESET LINK").assertExists()
        }

    @Test
    fun emptyEmailValidation_isDisplayed(){
        val vm = createTestViewModel()
        composeTestRule.setContent {
            ForgotPasswordScreen(onSendClick = {}, viewModel = vm)
        }
        composeTestRule.onNodeWithTag("Gradient_Btn_click").performClick()
        composeTestRule.onNodeWithText("Enter a Valid Email").assertExists()
        //composeTestRule.onNodeWithText("SEND RESET LINK").assertExists()
    }
}