package com.asphalt.android.di

import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.repository.UserRepoImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidSharedModule = module {
    single { UserRepoImpl() }
    single { AndroidUserVM(get(), get()) }
}