package com.asphalt.queries.di

import com.asphalt.queries.viewmodels.AskQueryVM
import com.asphalt.queries.viewmodels.QueriesVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val queryModule = module {
    viewModel { QueriesVM(get(), get()) }
    viewModel { AskQueryVM(get()) }

}