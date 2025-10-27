package com.asphalt.android

import android.app.Application
import com.asphalt.android.di.androidSharedModule
import com.asphalt.android.di.appModule
import com.asphalt.android.di.sharedModule
import com.asphalt.android.repository.AuthenticatorImpl
import com.asphalt.dashboard.di.dashboardModule
import com.asphalt.joinaride.di.joinRideModule
import com.asphalt.login.di.loginModule
import com.asphalt.profile.di.profileModule
import com.asphalt.queries.di.queryModule
import com.asphalt.registration.di.registrationModule
import com.asphalt.welcome.di.welcomeFeatureModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AsphaltApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            androidLogger()
            androidContext(androidContext = this@AsphaltApplication)
            //First start Koin with all modules including shared components
            modules(
                modules = appModule + loginModule + registrationModule + welcomeFeatureModule
                        + sharedModule + dashboardModule + profileModule + androidSharedModule + queryModule
                + joinRideModule,

                )
            module {
                single { AuthenticatorImpl() }
            }

        }
    }
}