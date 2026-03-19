package com.asphalt.android.repository

import com.asphalt.android.model.AuthResultimpl
import com.asphalt.android.model.LoginResult
import com.asphalt.android.model.User
import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRAuthDataResult
import cocoapods.FirebaseDatabase.FIRDataEventType
import cocoapods.FirebaseDatabase.FIRDatabase
import platform.Foundation.NSError
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSNumber
import platform.UIKit.UIDevice

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.time.Clock

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
actual class AuthenticatorImpl actual constructor() {

    val auth = FIRAuth.auth()

    actual suspend fun signUp(user: User): Result<String> = suspendCancellableCoroutine { cont ->
        val database = FIRDatabase.database().reference()
        auth.createUserWithEmail(user.email ?: "", user.confirmPassword ?: "") { result, error ->
            if (error != null) {
                cont.resume(Result.failure(Exception(error.localizedDescription ?: "Unknown error")))
                return@createUserWithEmail
            }

            val userId = result?.user()?.uid()
            if (userId == null) {
                cont.resume(Result.failure(Exception("User ID is null")))
                return@createUserWithEmail
            }

            val userValues = mapOf<Any?, Any?>(
                "email" to user.email,
                "user_name" to user.name,
                "device" to UIDevice.currentDevice.model,
                "created_date" to Clock.System.now().toEpochMilliseconds()
            )

            database.child("users").child(userId).setValue(userValues) { dbError: NSError?, _ ->
                if (dbError != null) {
                    cont.resume(Result.failure(Exception(dbError.localizedDescription ?: "Database update failed")))
                } else {
                    cont.resume(Result.success("Success"))
                }
            }
        }
    }

    actual suspend fun signIn(email: String, password: String): LoginResult =
        suspendCancellableCoroutine { cont ->

            auth.signInWithEmail(email = email, password = password) { result, error ->
                if (error != null) {
                    cont.resume(AuthResultimpl(false, error.localizedDescription ?: "Unknown error"))
                    return@signInWithEmail
                }

                val user = result?.user()
                if (user == null) {
                    cont.resume(AuthResultimpl(false, "User object is null"))
                    return@signInWithEmail
                }

                val dbRef = FIRDatabase.database().reference().child("users").child(user.uid())

                // Fetch the data using `observeSingleEventOfType` with .Value
                dbRef.observeSingleEventOfType(FIRDataEventType.FIRDataEventTypeValue) { snapshot, error ->

                    val data = snapshot?.value as? Map<Any?, *>
                    val name = data?.get("user_name") as? String
                    val emailValue = data?.get("email") as? String
                    val rawCreatedDate = data?.get("created_date")

                    val accountCreatedDate: Long = when (rawCreatedDate) {
                        is Long -> rawCreatedDate
                        is Int -> rawCreatedDate.toLong()
                        is Double -> rawCreatedDate.toLong()
                        is NSNumber -> rawCreatedDate.longLongValue
                        is String -> rawCreatedDate.toLongOrNull() ?: 0L
                        else -> 0L
                    }
                    println("RAW SNAPSHOT: ${snapshot?.value}")
                    println("TYPE: ${snapshot?.value?.let { it::class }}")
                    println("CREATED DATE RAW: $rawCreatedDate")
                    println("CREATED DATE TYPE: ${rawCreatedDate?.let { it::class }}")

                    cont.resume(
                        AuthResultimpl(
                            isSuccess = true,
                            errorMessage = null,
                            name = name,
                            email = emailValue,
                            uid = user.uid(),
                            accountCreatedDate = accountCreatedDate
                        )
                    )
                }
            }
        }
    actual suspend fun resetPassword(email: String): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            auth.sendPasswordResetWithEmail(email) { error: NSError? ->
                if (error == null) {
                    continuation.resume(Result.success("Password reset email sent successfully."))
                } else {
                    continuation.resumeWithException(
                        Exception(error.localizedDescription ?: "An unknown error occurred.")
                    )
                }
            }
        }
    }

    actual suspend fun logout(): Result<String> {
        val auth = FIRAuth.auth()
        return try {
            auth.signOut(null)
            Result.success("User logged out successfully.")
        } catch (e: Exception) {
            Result.failure(Exception("Error signing out: ${e.message}", e))
        }
    }

    actual suspend fun getToken(): Result<String> {
        TODO("Not yet implemented")
    }
}



