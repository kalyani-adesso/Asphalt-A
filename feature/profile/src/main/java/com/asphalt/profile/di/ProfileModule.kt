package com.asphalt.profile.di

import com.asphalt.profile.repositories.ProfileSectionRepo
import com.asphalt.profile.viewmodels.ProfileSectionVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single { ProfileSectionRepo() }
    viewModel { ProfileSectionVM(get()) }
}