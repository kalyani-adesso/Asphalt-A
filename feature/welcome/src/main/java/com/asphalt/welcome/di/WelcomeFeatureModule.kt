package com.asphalt.welcome.di

import com.asphalt.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val welcomeFeatureModule = module {
    viewModel { WelcomeViewModel() }
}