rootProject.name = "AsphaltA"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

include(":composeApp") // Android app module
include(":app",":shared") // KMP shared module
include(":feature")
include(":feature:registration")
include(":commonui")
include(":feature:login")
include(":feature:welcome")
include(":feature:dashboard")
include(":feature:createride")
include(":feature:profile")
include(":feature:queries")
include(":feature:resetpassword")
include(":feature:joinaride")
