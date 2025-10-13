import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
     alias(libs.plugins.google.gms.google.services)
    id("org.jetbrains.kotlin.native.cocoapods")
}

kotlin {

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    cocoapods {
        summary = "Shared module for Asphalt-A project"
        homepage = "https://github.com/kalyani-adesso/Asphalt-A"
        version = "1.0.0"
        ios.deploymentTarget = "13"
       name = "shared"
        val fmodules = listOf("-compiler-option", "-fmodules")
        pod("FirebaseCore") {
            extraOpts += fmodules
        }
        pod("FirebaseDatabase") {
            extraOpts += fmodules
        }
        pod("FirebaseFirestore") {
            extraOpts += fmodules
        }
        pod("FirebaseAuth") {
            extraOpts += fmodules
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)


//            implementation(libs.firebase.common) // Or latest version
//            implementation(libs.gitlive.firebase.auth)    // Optional: Firebase Auth
//            implementation(libs.firebase.firestore) // Optional: Firestore
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.koin.compose)
            implementation(libs.koin.android)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.coroutines)
            implementation(projects.commonui)

            //firebase
          //  implementation(libs.firebase.analytics)
            implementation(libs.androidx.credentials)
            implementation(libs.googleid)
            implementation(libs.firebase.database)
            implementation(libs.firebase.auth)
            implementation(libs.androidx.credentials.play.services.auth)
            implementation(libs.firebase.firestore.ktx)

            implementation(libs.datastore)

        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.ktor.client.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.asphalt.android.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}