package com.asphalt.profile.di

import com.asphalt.profile.repositories.ProfilesSectionRepo
import com.asphalt.profile.repositories.TotalStatsRepo
import com.asphalt.profile.repositories.YourVehiclesRepo
import com.asphalt.profile.viewmodels.AddBikesVM
import com.asphalt.profile.viewmodels.ProfileSectionVM
import com.asphalt.profile.viewmodels.TotalStatsVM
import com.asphalt.profile.viewmodels.YourVehiclesVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single { YourVehiclesRepo() }
    single { ProfilesSectionRepo() }
    single { TotalStatsRepo() }
    viewModel { ProfileSectionVM(get()) }
    viewModel { YourVehiclesVM(get()) }
    viewModel { TotalStatsVM(get()) }
    viewModel { AddBikesVM() }
}