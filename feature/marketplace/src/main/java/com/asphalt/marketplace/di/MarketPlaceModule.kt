package com.asphalt.marketplace.di

import com.asphalt.marketplace.viewmodel.ProductListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val marketPlaceModule = module {
    viewModel { ProductListViewModel() }


}