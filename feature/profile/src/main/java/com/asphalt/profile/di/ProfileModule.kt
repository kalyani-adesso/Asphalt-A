package com.asphalt.profile.di

import com.asphalt.profile.repositories.ProfilesDataRepo
import com.asphalt.profile.repositories.TotalStatsRepo
import com.asphalt.profile.viewmodels.AddBikesVM
import com.asphalt.profile.viewmodels.EditProfileVM
import com.asphalt.profile.viewmodels.ProfileSectionVM
import com.asphalt.profile.viewmodels.TotalStatsVM
import com.asphalt.profile.viewmodels.YourVehiclesVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single { ProfilesDataRepo() }
    single { TotalStatsRepo() }
    viewModel { ProfileSectionVM(get(),get()) }
    viewModel { YourVehiclesVM(get(), get()) }
    viewModel { TotalStatsVM(get(),get(),get()) }
    viewModel { AddBikesVM() }
    viewModel { EditProfileVM(get()) }
}