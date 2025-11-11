package com.asphalt.android.di


import com.asphalt.android.network.KtorClient
import com.asphalt.android.network.joinrides.JoinRidesApiService
import com.asphalt.android.network.joinrides.JoinRidesApiServiceImpl
import com.asphalt.android.network.profile.ProfileAPIService
import com.asphalt.android.network.profile.ProfileAPIServiceImpl
import com.asphalt.android.network.queries.QueryAPIService
import com.asphalt.android.network.queries.QueryAPIServiceImpl
import com.asphalt.android.network.rides.RidesApIService
import com.asphalt.android.network.rides.RidesApiServiceImpl
import com.asphalt.android.network.user.UserAPIService
import com.asphalt.android.network.user.UserAPIServiceImpl
import com.asphalt.android.repository.AuthenticatorImpl
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.joinride.JoinRideRepository
import com.asphalt.android.repository.profile.ProfileRepository
import com.asphalt.android.repository.queries.QueryRepository
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodel.AuthViewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.math.sin

val sharedModule: Module = module {
    single { KtorClient() }
    single<QueryAPIService> { QueryAPIServiceImpl(get()) }
    single<UserAPIService> { UserAPIServiceImpl(get()) }
    single<RidesApIService> { RidesApiServiceImpl(get()) }
    single<ProfileAPIService> { ProfileAPIServiceImpl(get()) }
    single { AuthenticatorImpl() }
    single { UserRepoImpl() }
    single { QueryRepository(get()) }
    factory { AuthViewModel(authenticator = get()) }
    single { RidesRepository(get()) }
    single { UserRepository(get()) }
    single { ProfileRepository(get()) }
    single { JoinRideRepository(get()) }
    single<JoinRidesApiService> { JoinRidesApiServiceImpl(get()) }

}