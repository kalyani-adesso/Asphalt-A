package com.asphalt.android.di


import com.asphalt.android.repository.AuthenticatorImpl
import com.asphalt.android.viewmodel.AuthViewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val sharedModule : Module = module {

    single { AuthenticatorImpl() }
    factory { AuthViewModel(authenticator = get()) }
}