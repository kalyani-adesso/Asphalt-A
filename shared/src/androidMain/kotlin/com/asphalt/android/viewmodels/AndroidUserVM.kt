package com.asphalt.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.helpers.UserDataHelper
import com.asphalt.android.model.CurrentUser
import com.asphalt.android.model.UserDomain
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.user.UserRepository
import com.asphalt.android.viewmodel.UserViewModel
import com.asphalt.commonui.constants.PreferenceKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class AndroidUserVM(
    userRepoImpl: UserRepoImpl,
    val dataStoreManager: DataStoreManager,
    val userAPIRepository: UserRepository
) : ViewModel() {


    private val sharedVM = UserViewModel(userRepoImpl, viewModelScope)
    val userState = sharedVM.user
    private val _userList = MutableStateFlow<List<UserDomain>>(emptyList())
    val userList = _userList.asStateFlow()


    init {
        sharedVM.userRepoImpl.setDataStoreManager(dataStoreManager)
        initialiseUserData()
        getAllUsers()
    }

    private fun getAllUsers() {
        viewModelScope.launch {
            APIHelperUI.handleApiResult(
                userAPIRepository.getAllUsers(),
                viewModelScope,
                showError = false
            ) {
                _userList.value = it
            }
        }

    }

    fun getUser(userID: String): UserDomain? {
        return UserDataHelper.getUserDataFromCurrentList(_userList.value, userID)
    }

    fun getCurrentUserUID(): String {
        return userState.value?.uid.orEmpty()
    }

    fun initialiseUserData() {
        sharedVM.fetchDetails()
    }

    suspend fun updateUserData(currentUser: CurrentUser) {
        val jsonString = Json.encodeToString(currentUser)
        dataStoreManager.saveValue(PreferenceKeys.USER_DETAILS, jsonString)
        initialiseUserData()
        getAllUsers()
    }

}