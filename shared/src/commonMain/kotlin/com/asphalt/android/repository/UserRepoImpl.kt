package com.asphalt.android.repository

import com.asphalt.android.model.CurrentUser

expect class UserRepoImpl() : UserRepo {
    override suspend fun getUserDetails(): CurrentUser?
}