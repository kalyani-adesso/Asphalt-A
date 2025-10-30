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
import kotlinx.serialization.json.*

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
        val url = "$baseUrl/users/$userId.json"

        return try {
            val jsonText: String = client.get(url).body()  // 🔹 get raw JSON string
            val json = Json.parseToJsonElement(jsonText).jsonObject

            ProfileInfo(
                userName = json["userName"]?.jsonPrimitive?.contentOrNull ?: "",
                email = json["email"]?.jsonPrimitive?.contentOrNull ?: "",
                phoneNumber = json["phoneNumber"]?.jsonPrimitive?.contentOrNull ?: "",
                emergencyContact = json["emergencyContact"]?.jsonPrimitive?.contentOrNull ?: "",
                drivingLicense = json["drivingLicense"]?.jsonPrimitive?.contentOrNull ?: "",
                isMechanic = json["isMechanic"]?.jsonPrimitive?.contentOrNull ?: "false"
            )
        } catch (e: Exception) {
            println(" Error parsing profile: ${e.message}")
            null
        }
    }

    actual suspend fun getBikes(userId: String): List<BikeInfo> {
        val url = "$baseUrl/users/$userId/bikes.json"
        val map: Map<String, BikeInfo>? = client.get(url).body()
        return map?.map { (bikeId, bikeInfo) ->
            bikeInfo.copy(bikeId = bikeId)
        } ?: emptyList()
    }

    actual suspend fun editProfile(
        userId: String,
        profile: ProfileInfo
    ) {
        val url = "$baseUrl/users/$userId.json"

        val updateData = mapOf(
            "email" to profile.email,
            "userName" to profile.userName,
            "device" to UIDevice.currentDevice.model,
            "phoneNumber" to profile.phoneNumber,
            "emergencyContact" to profile.emergencyContact,
            "drivingLicense" to profile.drivingLicense,
            "isMechanic" to profile.isMechanic.toString()
        )

        val response = client.patch(url) {
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
