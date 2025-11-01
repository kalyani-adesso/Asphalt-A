package com.asphalt.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.model.CurrentUser
import com.asphalt.android.model.User
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.viewmodel.UserViewModel
import com.asphalt.commonui.constants.PreferenceKeys
import kotlinx.serialization.json.Json

class AndroidUserVM(userRepoImpl: UserRepoImpl, val dataStoreManager: DataStoreManager) : ViewModel() {


    private val sharedVM = UserViewModel(userRepoImpl, viewModelScope)
    val userState = sharedVM.user


    init {
        sharedVM.userRepoImpl.setDataStoreManager(dataStoreManager)
        initialiseUserData()
    }

    fun initialiseUserData() {
        sharedVM.fetchDetails()
    }

    suspend fun updateUserData(currentUser: CurrentUser) {
        val jsonString = Json.encodeToString(currentUser)
        dataStoreManager.saveValue(PreferenceKeys.USER_DETAILS, jsonString)
        initialiseUserData()
    }

}