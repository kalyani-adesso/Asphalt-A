package com.asphalt.android.di


import com.asphalt.android.network.ApiService
import com.asphalt.android.network.KtorClient
import com.asphalt.android.repository.AuthenticatorImpl
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.viewmodel.AuthViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule: Module = module {
    single { AuthenticatorImpl() }
    single { UserRepoImpl() }
    single { KtorClient() }
    single { ApiService(get()) }
    factory { AuthViewModel(authenticator = get()) }
}