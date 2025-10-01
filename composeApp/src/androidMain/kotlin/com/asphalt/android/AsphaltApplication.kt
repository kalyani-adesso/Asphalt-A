package com.asphalt.android

import android.app.Application
import com.asphalt.registration.di.registrationModule
import com.asphalt.welcome.di.welcomeFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AsphaltApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(androidContext = this@AsphaltApplication)
            //First start Koin with all modules including shared components
            modules(
                registrationModule,
                welcomeFeatureModule
            )

        }
    }
}