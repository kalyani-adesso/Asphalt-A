package com.asphalt.login.di

import com.asphalt.login.model.LoginValidationModel
import com.asphalt.login.viewmodel.LoginScreenViewModel
import org.koin.dsl.module

val loginModule= module {
    single { LoginScreenViewModel() }
}