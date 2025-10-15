package com.asphalt.login.di


import com.asphalt.login.model.LoginValidationModel
import com.asphalt.login.viewmodel.LoginScreenViewModel
import org.koin.core.context.GlobalContext.get
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    viewModel { LoginScreenViewModel(get(),get()) }
}