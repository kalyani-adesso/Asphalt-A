package com.asphalt.android.di


import com.asphalt.android.network.KtorClient
import com.asphalt.android.network.queries.QueryAPIService
import com.asphalt.android.network.queries.QueryAPIServiceImpl
import com.asphalt.android.network.user.UserAPIService
import com.asphalt.android.network.user.UserAPIServiceImpl
import com.asphalt.android.repository.AuthenticatorImpl
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.queries.QueryRepository
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodel.AuthViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule: Module = module {
    single { KtorClient() }
    single<QueryAPIService> { QueryAPIServiceImpl(get()) }
    single<UserAPIService> { UserAPIServiceImpl(get()) }
    single { AuthenticatorImpl() }
    single { UserRepoImpl() }
    single { QueryRepository(get()) }
    factory { AuthViewModel(authenticator = get()) }
    factory { UserRepository(get()) }
}