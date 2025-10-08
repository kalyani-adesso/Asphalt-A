package com.asphalt.registration.di

import com.asphalt.registration.viewmodel.RegistrationCodeViewModel
import com.asphalt.registration.viewmodel.RegistrationDetailsViewModel
import com.asphalt.registration.viewmodel.RegistrationPasswordViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val registrationModule = module {
    viewModel { RegistrationCodeViewModel() }

    viewModel { RegistrationPasswordViewModel() }

    viewModel { RegistrationDetailsViewModel() }
}