package com.asphalt.createride.di

import com.asphalt.createride.viewmodel.PlacesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val createRideModule = module {

    viewModel { PlacesViewModel(get()) }
}