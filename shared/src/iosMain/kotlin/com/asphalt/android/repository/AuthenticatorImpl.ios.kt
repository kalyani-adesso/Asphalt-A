package com.asphalt.android.repository

import com.asphalt.android.model.AuthResultimpl
import com.asphalt.android.model.LoginResult
import com.asphalt.android.model.User
import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRAuthDataResult
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
                "userName" to user.name,
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


    actual suspend fun signIn(email: String, password: String): LoginResult = suspendCancellableCoroutine { cont ->
        auth.signInWithEmail(email = email, password = password) { result: FIRAuthDataResult?, error: NSError? ->
            if (error != null) {
                cont.resume(AuthResultimpl(false, error.localizedDescription ?: "Unknown error"))
                return@signInWithEmail
            }

            val user = result?.user()
            if (user != null) {
                val loginResult = AuthResultimpl(
                    isSuccess = true,
                    errorMessage = null,
                    name = user.displayName(),
                    email = user.email(),
                    uid = user.uid()
                )
                cont.resume(loginResult)
            } else {
                cont.resume(AuthResultimpl(false, "Sign-in succeeded but user object was null."))
            }
        }
    }
}



