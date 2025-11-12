package com.asphalt.android.di

import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin() {

    startKoin {
        modules(sharedModule)
    }
}
