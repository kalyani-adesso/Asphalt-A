package com.asphalt.android.repository

import com.asphalt.android.model.BikeInfo
import com.asphalt.android.model.ProfileInfo

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import platform.UIKit.UIDevice

actual class FirebaseApi {

    private val client = HttpClient(Darwin) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }
    private val baseUrl = "https://asphalt-a-d59cb-default-rtdb.firebaseio.com"

    actual suspend fun createProfile(userId: String, profile: ProfileInfo) {
        client.post("$baseUrl/users/$userId/profile.json") {
            contentType(ContentType.Application.Json)
            setBody(profile)
        }
    }

    actual suspend fun addBike(userId: String, bike: BikeInfo) {
        client.post("$baseUrl/users/$userId/bikes.json") {
            contentType(ContentType.Application.Json)
            setBody(bike)
        }
    }

    actual suspend fun deleteBike(userId: String, bikeId: String) {
        client.delete("$baseUrl/users/$userId/bikes/$bikeId.json")
    }

    actual suspend fun getProfile(userId: String): ProfileInfo? {
        return client.get("$baseUrl/users/$userId/profile.json").body()
    }

    actual suspend fun getBikes(userId: String): List<BikeInfo> {
        val map: Map<String, BikeInfo>? = client.get("$baseUrl/users/$userId/bikes.json").body()
        return map?.values?.toList() ?: emptyList()
    }

    actual suspend fun editProfile(
        userId: String,
        email: String,
        userName: String,
        profile: ProfileInfo
    ) {
        val url = "$baseUrl/users/$userId.json"

        // Combined update: top-level email, userName + full profile
        val updateData = mapOf(
            "email" to email,
            "userName" to userName,
            "device" to UIDevice.currentDevice.model,
            "profile" to profile,
        )

        val response = client.put(url) {
            contentType(ContentType.Application.Json)
            setBody(updateData)
        }

        if (response.status.isSuccess()) {
            println("Profile, email, and userName updated successfully for user: $userId")
        } else {
            println(" Failed to update user info. HTTP Status: ${response.status}")
        }
    }
}
