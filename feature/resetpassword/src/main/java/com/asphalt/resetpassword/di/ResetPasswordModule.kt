package com.asphalt.resetpassword.di


import com.asphalt.resetpassword.viewmodel.ForgotPasswordViewModel
import com.asphalt.resetpassword.viewmodel.VerifyCodeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val resetModule = module {
    viewModel {ForgotPasswordViewModel() }
    viewModel { VerifyCodeViewModel() }
}