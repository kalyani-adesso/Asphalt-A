package com.asphalt.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.viewmodel.UserViewModel

class AndroidUserVM(userRepoImpl: UserRepoImpl, dataStoreManager: DataStoreManager) : ViewModel() {


    private val sharedVM = UserViewModel(userRepoImpl, viewModelScope)

    init {
        sharedVM.userRepoImpl.setDataStoreManager(dataStoreManager)
        initialiseUserData()
    }

    fun initialiseUserData() {
        sharedVM.fetchDetails()
    }

    val userState = sharedVM.user
}