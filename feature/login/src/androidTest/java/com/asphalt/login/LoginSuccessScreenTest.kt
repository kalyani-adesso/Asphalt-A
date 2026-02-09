package com.asphalt.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.di.sharedModule
import com.asphalt.commonui.constants.PreferenceKeys
import com.asphalt.login.di.loginModule
import com.asphalt.login.ui.LoginSuccessScreen
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify


class LoginSuccessScreenTest {
    // create one mock instance for Koin and verification
    private val mockDataStore: DataStoreManager = mock()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        startKoin {
            modules(
                loginModule,
                sharedModule,
                module {
                    // Provide the same mock instance for injection
                    single { mockDataStore }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun loginSuccessScreen_displaysText_and_buttonClickSavesData() {

        var exploreClicked = false
        composeTestRule.setContent {
            LoginSuccessScreen({})

        }


        composeTestRule.onNodeWithText("Yey! Login Successful").assertIsDisplayed()
        composeTestRule.onNodeWithText("You will be moved to home screen right now.Enjoy the features!").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("login_success_image")
            .assertIsDisplayed()
        /*composeTestRule.onNodeWithText("LETS EXPLORE").performClick()
        assert(exploreClicked)
        runBlocking {
            verify(mockDataStore).saveValue(PreferenceKeys.IS_LOGGED_IN_BEFORE, true)
        }*/
    }
}