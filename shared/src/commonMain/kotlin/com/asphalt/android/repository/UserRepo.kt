package com.asphalt.android.repository

import com.asphalt.android.model.CurrentUser

interface UserRepo {
    suspend fun getUserDetails(): CurrentUser?
}