package com.asphalt.android.di


import com.asphalt.android.repository.AuthenticatorImpl
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.android.viewmodel.UserViewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sharedModule: Module = module {
    single { AuthenticatorImpl() }
    single { UserRepoImpl() }
    factory { AuthViewModel(authenticator = get()) }
}