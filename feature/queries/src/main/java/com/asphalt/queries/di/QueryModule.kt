package com.asphalt.queries.di

import com.asphalt.queries.repositories.QueryRepo
import com.asphalt.queries.viewmodels.AskQueryVM
import com.asphalt.queries.viewmodels.QueriesVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val queryModule = module {
    single { QueryRepo() }
    viewModel { QueriesVM(get(),get()) }
    viewModel { AskQueryVM(get()) }

}