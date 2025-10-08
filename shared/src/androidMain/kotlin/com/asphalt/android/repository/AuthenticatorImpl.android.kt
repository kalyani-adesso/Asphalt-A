package com.asphalt.android.repository

import com.asphalt.android.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

actual class AuthenticatorImpl {

    actual suspend fun signUp(user: User): Result<String> {

        return try {
            val result = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user.email,user.password)
                .await()

            val uId = result.user?.uid ?: return Result.failure(Exception("User Id missing"))
            // store user data into realtime db
            val userToSave = User(
                email = user.email,
                password = user.password,
                userName = user.userName,
                confirmPassword = user.confirmPassword
            )

            val dbRef = FirebaseDatabase.getInstance()
                .getReference("users_android")
                .child(uId)

            dbRef.setValue(userToSave).await()

            Result.success(uId)

        }catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}