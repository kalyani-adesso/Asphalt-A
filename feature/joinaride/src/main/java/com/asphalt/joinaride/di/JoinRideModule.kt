package com.asphalt.joinaride.di

import com.asphalt.android.repository.joinride.JoinRideRepository
import com.asphalt.android.viewmodel.joinridevm.JoinRideViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
val joinRideModule = module {
    viewModel { JoinRideViewModel(get()) }
    single { JoinRideRepository() }
}