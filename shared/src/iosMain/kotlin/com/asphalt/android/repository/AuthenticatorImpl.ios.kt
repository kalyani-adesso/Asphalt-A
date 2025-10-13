package com.asphalt.android.repository

import com.asphalt.android.model.User
import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseDatabase.FIRDatabase
import platform.Foundation.NSError
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIDevice
import kotlin.coroutines.resume

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
actual class AuthenticatorImpl actual constructor() {

    val auth = FIRAuth.auth()

    actual suspend fun signUp(user: User): Result<String> = suspendCancellableCoroutine { cont ->
        val database = FIRDatabase.database().reference()
        auth.createUserWithEmail(user.email, user.confirmPassword) { result, error ->
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
                "userName" to user.userName,
                "device" to UIDevice.currentDevice.model
            )

            database.child("users").child(userId).updateChildValues(userValues) { dbError: NSError?, _ ->
                if (dbError != null) {
                    cont.resume(Result.failure(Exception(dbError.localizedDescription ?: "Database update failed")))
                } else {
                    cont.resume(Result.success("Success"))
                }
            }
        }
    }
}



