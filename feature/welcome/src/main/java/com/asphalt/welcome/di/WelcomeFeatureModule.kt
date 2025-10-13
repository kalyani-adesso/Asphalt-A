package com.asphalt.welcome.di

import com.asphalt.welcome.repositories.AppUsageRepository
import com.asphalt.welcome.viewmodels.WelcomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val welcomeFeatureModule = module {
    single { AppUsageRepository(get()) }
    viewModel { WelcomeViewModel(get()) }
}