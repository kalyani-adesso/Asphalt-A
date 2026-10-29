package com.asphalt.login

import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.login.viewmodel.LoginScreenViewModel
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginScreenViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var authViewModel: AuthViewModel

    @Mock
    lateinit var dataStoreManager: DataStoreManager

    @Mock
    lateinit var androidUserVM: AndroidUserVM

    private lateinit var viewModel: LoginScreenViewModel

    @Before
    fun setup() {
        viewModel = LoginScreenViewModel(
            authViewModel,
            dataStoreManager,
            androidUserVM
        )
    }

    @Test
    fun updateEmailState_validEmail_shouldUpdateState() {

        viewModel.updateEmailState("test@gmail.com")

        assertEquals(
            "test@gmail.com",
            viewModel.emailTextState.value
        )

        assertTrue(viewModel.isEmailVaild.value)
    }

    @Test
    fun updateEmailState_invalidEmail_shouldSetFalse() {

        viewModel.updateEmailState("test")

        assertFalse(viewModel.isEmailVaild.value)
    }

    @Test
    fun updatePassword_shouldUpdateState() {

        viewModel.updatePassword("123456")

        assertEquals(
            "123456",
            viewModel.passwordTextState.value
        )
    }

    @Test
    fun fieldValidation_emptyEmail_shouldReturnFalse() {

        val result = viewModel.fieldValidation()

        assertFalse(result)
        assertTrue(
            viewModel.validateState.value.isShowEmailError
        )
    }

    @Test
    fun fieldValidation_invalidEmail_shouldReturnFalse() {

        viewModel.updateEmailState("abc")

        val result = viewModel.fieldValidation()

        assertFalse(result)
        assertTrue(
            viewModel.validateState.value.isShowEmailError
        )
    }

    @Test
    fun fieldValidation_emptyPassword_shouldReturnFalse() {

        viewModel.updateEmailState("test@gmail.com")

        val result = viewModel.fieldValidation()

        assertFalse(result)
        assertTrue(
            viewModel.validateState.value.isShowPasswordError
        )
    }

    @Test
    fun fieldValidation_validFields_shouldReturnTrue() {

        viewModel.updateEmailState("test@gmail.com")
        viewModel.updatePassword("123456")

        val result = viewModel.fieldValidation()

        assertTrue(result)
    }
}