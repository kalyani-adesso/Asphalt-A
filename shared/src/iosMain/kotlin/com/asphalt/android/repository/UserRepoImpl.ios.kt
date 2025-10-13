package com.asphalt.android.repository

import com.asphalt.android.model.CurrentUser

actual class UserRepoImpl actual constructor(): UserRepo{
    actual override suspend fun getUserDetails(): CurrentUser? {
        return null
    }

}