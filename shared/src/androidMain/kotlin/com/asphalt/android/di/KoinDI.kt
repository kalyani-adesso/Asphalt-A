package com.asphalt.android.di

import org.koin.core.context.startKoin

fun initKoin() {

    startKoin {
        modules(sharedModule)
    }
}