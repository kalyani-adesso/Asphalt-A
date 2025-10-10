package com.asphalt.android.repository

import android.util.Log
import com.asphalt.android.model.AuthResultimpl
import com.asphalt.android.model.LoginResult
import com.asphalt.android.model.User
import com.asphalt.commonui.utils.Constants
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await

actual class AuthenticatorImpl {

    actual suspend fun signUp(user: User): Result<String> {

        return try {
            val result = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user.email, user.password)
                .await()

            val uId = result.user?.uid ?: return Result.failure(Exception("User Id missing"))
            // store user data into realtime db
            val userToSave = User(
                email = user.email,
                password = user.password,
                name = user.name,
                confirmPassword = user.confirmPassword
            )

            val dbRef = FirebaseDatabase.getInstance()
                .getReference("users_android")
                .child(uId)

            dbRef.setValue(userToSave).await()

            Result.success(uId)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

//    actual suspend fun signIn(email: String, password: String): LoginResult {
//        val auth: FirebaseAuth = FirebaseAuth.getInstance()
//        return try {
//            auth.signInWithEmailAndPassword(email, password).await()
//            AuthResultimpl(true,"")
//
//        } catch (e: Exception) {
//            AuthResultimpl(false,"User not found")
//        }
//    }

    actual suspend fun signIn(email: String, password: String): LoginResult {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        return try {
            auth.signInWithEmailAndPassword(email, password).await()

            val user = auth.currentUser
            val id = user?.uid ?: return AuthResultimpl(false, "User ID not found")

            val userRef = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_DB)
                .child(id)

            val dataSnapshot = userRef.get().await()
            //val userData = dataSnapshot.getValue(User::class.java)
            val name = dataSnapshot.child(Constants.Firebase_user_name).getValue(String::class.java)
            val email = dataSnapshot.child(Constants.Firebase_user_email).getValue(String::class.java)

            //Log.d("name","name ${name}")

            AuthResultimpl(
                isSuccess = true,
                errorMessage = "",
                name = name,
                email = email,
                uid = id
            )

        } catch (e: Exception) {
            AuthResultimpl(false, "Authentication failed: ${e.message}")
        }
    }
}