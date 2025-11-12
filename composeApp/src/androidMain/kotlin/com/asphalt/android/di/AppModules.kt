package com.asphalt.android.di

import com.asphalt.android.datastore.DataStoreManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { DataStoreManager(androidContext()) }
    // single<MyRepository> { MyRepositoryImpl(get()) }
    // viewModel<MyViewModel> { MyViewModel(get()) }
}