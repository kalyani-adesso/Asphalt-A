package com.asphalt.android.repository

import com.asphalt.android.model.CurrentUser
import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseDatabase.FIRDatabase
import cocoapods.FirebaseDatabase.FIRDataEventType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual class UserRepoImpl actual constructor() : UserRepo {

    actual override suspend fun getUserDetails(): CurrentUser? = suspendCancellableCoroutine { cont ->
        val auth = FIRAuth.auth()
        val user = auth.currentUser() ?: run {
            cont.resume(null)
            return@suspendCancellableCoroutine
        }

        val uid = user.uid()

        val email = user.email() ?: ""

        val ref = FIRDatabase.database().reference()
            .child("users")
            .child(uid)

        ref.observeSingleEventOfType(
            eventType = FIRDataEventType.FIRDataEventTypeValue,
            withBlock = { snapshot ->
                val data = snapshot?.value() as? Map<*, *>
                val name = (data?.get("user_name")) as? String ?: "Guest"

                cont.resume(
                    CurrentUser(
                        uid = uid,
                        name = name,
                        email = email,
                        isSuccess = true,
                        errorMessage = null
                    )
                )
            },
            withCancelBlock = { error: NSError? ->
                println("Firebase failed: ${error?.localizedDescription}")
                cont.resume(
                    CurrentUser(
                        uid = uid,
                        name = "Guest",
                        email = email,
                        isSuccess = false,
                        errorMessage = error?.localizedDescription
                    )
                )
            }
        )
    }
}
