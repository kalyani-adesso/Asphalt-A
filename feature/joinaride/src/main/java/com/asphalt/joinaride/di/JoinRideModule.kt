package com.asphalt.joinaride.di

import com.asphalt.android.repository.joinride.JoinRideRepository
import com.asphalt.android.repository.joinride.RidersGroupRepo
import com.asphalt.joinaride.viewmodel.JoinRideViewModel
import com.asphalt.android.viewmodel.joinridevm.RidersGroupViewModel
import com.asphalt.android.viewmodel.joinridevm.RidesDifficultyViewModel
import com.asphalt.joinaride.viewmodel.RatingViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
val joinRideModule = module {
    viewModel { JoinRideViewModel(get()) }
    viewModel { RatingViewModel() }
    viewModel { RidesDifficultyViewModel() }
    viewModel { RidersGroupViewModel(get()) }
    single { JoinRideRepository(get()) }
    single { RidersGroupRepo() }
}