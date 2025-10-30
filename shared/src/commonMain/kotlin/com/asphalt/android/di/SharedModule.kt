package com.asphalt.android.di


import com.asphalt.android.network.APIService
import com.asphalt.android.network.APIServiceImpl
import com.asphalt.android.network.KtorClient
import com.asphalt.android.repository.AuthenticatorImpl
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.queries.QueryRepository
import com.asphalt.android.viewmodel.AuthViewModel
import com.asphalt.android.viewmodel.UserViewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sharedModule: Module = module {
    single { KtorClient() }
    single<APIService> { APIServiceImpl(get()) }
    single { AuthenticatorImpl() }
    single { UserRepoImpl() }
    single { QueryRepository(get()) }
    factory { AuthViewModel(authenticator = get()) }
}