package com.asphalt.dashboard.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.constants.APIConstants
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.RidersList
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.R
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RidesDetailsViewModel() : ViewModel(), KoinComponent {
    val ridesRepo: RidesRepository by inject()
    val userRepoImpl: UserRepoImpl by inject()
    val androidUserVM: AndroidUserVM by inject()
    private val ridesDetails = mutableStateOf<RidesData?>(null)
    val ridesData: State<RidesData?> = ridesDetails

    private val _ridersList = mutableStateOf<List<RidersList>>(emptyList())
    val ridersList: State<List<RidersList>> = _ridersList
    val showDeleteButton = mutableStateOf(false)

    fun getSingleRide(ridesId: String) {
        viewModelScope.launch {
            val apiResult = APIHelperUI.runWithLoader {
                ridesRepo.getSingeRide(ridesId)

            }
            APIHelperUI.handleApiResult(apiResult, viewModelScope) { response ->
                ridesDetails.value = response
                getUserList()
            }
        }

    }

    fun getUserList() {
        viewModelScope.launch {
            var user = userRepoImpl.getUserDetails()
            val users: List<UserDomain> = androidUserVM.userList.value

            val userList = ridesDetails.value?.participants ?: emptyList()
            val list = ArrayList(userList.mapNotNull { participant ->
                //if (participant.inviteStatus == APIConstants.END_RIDE) return@mapNotNull null

                users.find { it.uid == participant.userId }?.let { user ->
                    RidersList(
                        uid = user.uid,
                        name = user.name,
                        profilePic = user.profilePic,
                        isOrganizer = false,
                        inviteStatus = participant.inviteStatus,
                        displayStatusString =
                            when (participant.inviteStatus) {
                                APIConstants.RIDE_INVITED -> R.string.waiting_response
                                APIConstants.RIDE_ACCEPTED, APIConstants.RIDE_JOINED, APIConstants.END_RIDE -> R.string.confirmed
                                APIConstants.RIDE_DECLINED -> R.string.decline
                                else -> R.string.empty_string

                            }

                    )
                }
            })

            val organizer = ridesDetails.value?.createdBy ?: ""
            if (!organizer.isNullOrEmpty()) {
                val organizerData = users.find { it.uid == organizer }
                var name = if (organizerData?.uid == user?.uid) {
                    showDeleteButton.value = true
                    "You"
                } else {
                    showDeleteButton.value = false
                    organizerData?.name

                }
                val temp = RidersList(
                    organizerData?.uid ?: "",
                    name ?: "",//organizerData?.name ?: "",
                    organizerData?.profilePic ?: "",
                    true,
                    APIConstants.RIDE_ACCEPTED,
                    R.string.ride_creator
                )
                list.add(0, temp)
            }

            _ridersList.value = list
        }
    }
}