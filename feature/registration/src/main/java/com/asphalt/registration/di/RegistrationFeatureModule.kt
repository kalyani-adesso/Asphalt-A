package com.asphalt.registration.di

import com.asphalt.registration.viewmodel.RegistrationCodeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val registrationModule = module {
    viewModel { RegistrationCodeViewModel() }
}