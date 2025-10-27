import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.google.gms.google.services)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.lifecycle.viewmodelCompose)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.ktor.client.core)
            //  implementation(libs.firebase.ios)

            implementation(projects.shared)

            //firebase
            implementation(libs.androidx.credentials)
            implementation(libs.googleid)
            implementation(libs.firebase.database)
            implementation(libs.firebase.auth)
            implementation(libs.androidx.credentials.play.services.auth)
            //  implementation(libs.google.firebase.firestore.ktx)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.asphalt.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.asphalt.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)

    // modules
    implementation(projects.shared)
    implementation(projects.feature.registration)
    implementation(projects.feature.login)
    implementation(projects.feature.welcome)
    implementation(projects.feature.dashboard)
    implementation(projects.feature.profile)
    implementation(projects.commonui)
    implementation(projects.feature.createride)
    implementation(projects.feature.queries)
    implementation(projects.feature.resetpassword)

    implementation(projects.feature.joinaride)

    //dependencies
    implementation(libs.androidx.core.splashscreen) // Splash Screen
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.androidx.material.icons.extended.android)
    implementation(libs.kotlinx.serialization.core)

    // KOIN
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)


}

