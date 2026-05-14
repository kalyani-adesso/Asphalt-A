package com.asphalt.dashboard.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.FirebaseServerValue
import com.asphalt.android.PlatformDatabase
import com.asphalt.android.constants.APIConstants
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.rides.ImageData
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.constants.RideStatConstants
import com.asphalt.dashboard.data.GalleryModel
import com.asphalt.dashboard.data.YourRideDataModel
import com.asphalt.dashboard.data.YourRideRoot
import com.asphalt.dashboard.utils.RidesFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Callback
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar


open class RidesScreenViewModel(val androidUserVM: AndroidUserVM) : ViewModel(), KoinComponent {
    private val currentUid = androidUserVM.userState.value?.uid
    val ridesRepo: RidesRepository by inject()
    val userRepoImpl: UserRepoImpl by inject()

    private val _tabSelectionMutableFlow = MutableStateFlow(RideStatConstants.UPCOMING_RIDE)
    val tabSelectFlow: StateFlow<Int> = _tabSelectionMutableFlow

    private val _ridesListMutableState = MutableStateFlow(YourRideRoot())
    val ridesListState: StateFlow<YourRideRoot> = _ridesListMutableState

    private val _showInviteNotification = MutableStateFlow(false)
    val showInviteNotification: StateFlow<Boolean> = _showInviteNotification

    private val _showChatDialog = MutableStateFlow(false)
    val showChatDialog: StateFlow<Boolean> = _showChatDialog

    private val _showNodata = MutableStateFlow(false)
    val showNodata: StateFlow<Boolean> = _showNodata


    fun updateTab(tab: Int) {
        _tabSelectionMutableFlow.value = tab
    }

    fun updateShowNodata(value: Boolean) {
        _showNodata.value = value
    }

    fun updateInviteStatus(isShow: Boolean) {
        _showInviteNotification.value = isShow
    }

    fun getRides() {

        viewModelScope.launch {
            //val currentTime = Calendar.getInstance().timeInMillis
            val currentTime = Utils.currentDateWithoutTime()

            var user = userRepoImpl.getUserDetails()
            val apiResult = APIHelperUI.runWithLoader {
                ridesRepo.getAllRide()
            }
            APIHelperUI.handleApiResult(apiResult, viewModelScope) { response ->
                val sortedArray = response.sortedBy { it.startDate }

                var upcoming =
                    RidesFilter.getUComingRides(sortedArray, user?.uid ?: "", androidUserVM)
                var invite = RidesFilter.getInvites(sortedArray, user?.uid ?: "", androidUserVM)
                var history = RidesFilter.getHistoryRide(sortedArray, user?.uid ?: "")
                var upcomiList = ArrayList<YourRideDataModel>()
                var inviteList = ArrayList<YourRideDataModel>()
                var historyList = ArrayList<YourRideDataModel>()

                upcomiList.addAll(upcoming)
                inviteList.addAll(invite)
                historyList.addAll(history)

                if (upcomiList.isNotEmpty()) {
                    upcomiList.removeAll { ride ->
                        ride.startDate?.let { it < currentTime } ?: false
                    }
                }

                if (inviteList.isNotEmpty()) {
                    inviteList.removeAll { ride ->
                        ride.startDate?.let { it < currentTime } ?: false
                    }
                }
                if (inviteList.isNotEmpty()) {
                    updateInviteStatus(true)
                } else {
                    updateInviteStatus(false)
                }
                var ridesList =
                    YourRideRoot(
                        upcoming = upcomiList,
                        invite = inviteList,
                        history = historyList
                    )//history = hirtoryList, invite = inviteList
                _ridesListMutableState.value = ridesList
            }
            //response.mapApiResult { it }
        }

    }

    fun acceptDeclined(rideID: String, status: Int) {
        viewModelScope.launch {
            currentUid?.let {
                APIHelperUI.handleApiResult(
                    APIHelperUI.runWithLoader {
                        ridesRepo.changeRideInviteStatus(rideID, currentUid, status)
                    }, viewModelScope
                ) {
                    getRides()
                }

            }

        }


    }

    fun updateImages(images: ArrayList<GalleryModel>, ridesID: String) {
        val currentState = _ridesListMutableState.value

        // find particular ride in history
        val updatedHistory = currentState.history.map { ride ->
            if (ride.ridesId == ridesID) {
                ride.copy(
                    images = ArrayList(images) // replace images
                )
            } else ride
        }.toCollection(ArrayList())

        // update state with new root object
        _ridesListMutableState.value = currentState.copy(
            history = updatedHistory
        )
    }

    fun uploadImages(rideID: String, image: List<String>) {
        // var list = listOf("1234", "123456", "12345678", "123456789", "1234567890", "11114444")
        viewModelScope.launch {
            APIHelperUI.handleApiResult(
                APIHelperUI.runWithLoader {
                    ridesRepo.uploadImage(rideID, image)
                }, viewModelScope
            ) {
                ridesRepo.updateImageCount(image.size, rideID)

                getRides()
            }
        }
    }

    fun deleteImage(ridesID: String, imageID: String) {
        viewModelScope.launch {
            APIHelperUI.handleApiResult(
                APIHelperUI.runWithLoader {
                    ridesRepo.deleteImage(ridesID, imageID)
                }, viewModelScope
            ) {
                ridesRepo.updateImageCount(-1, ridesID)


                //getRides()
            }
        }
    }

    fun fetchImages(ridesId: String?, imageFetchCallback: (ArrayList<ImageData>) -> Unit) {
        viewModelScope.launch {

            ridesId?.let {
                APIHelperUI.handleApiResult(
                    APIHelperUI.runWithLoader { ridesRepo.fetchImages(it) }, viewModelScope
                ) { imageList ->
                    val imageArrayList = ArrayList(imageList)
                    imageFetchCallback.invoke(imageArrayList)
                }
            }

        }
    }

//    fun getRides() {
//        var upcoming = YourRideDataModel(
//            title = "Kanyakumari Trip",
//            place = "Kochi - Kanyakumari",
//            rideStatus = "Upcoming".uppercase(),
//            date = "Sun, Oct 26 - 02:00 PM", 2
//        )
//        var upcoming1 = YourRideDataModel(
//            title = "Trip to Moonnar",
//            place = "Kochi - Moonnar",
//            rideStatus = "Queue".uppercase(),
//            date = "Sun, Oct 26 - 04:00 AM", 2
//        )
//        var upcoming2 = YourRideDataModel(
//            title = "Weekend Coast Ride",
//            place = "Kochi - Vagamon",
//            rideStatus = "Queue".uppercase(),
//            date = "Thu, Oct 23 - 05:00 AM", 2
//        )
//
//        var hirtory = YourRideDataModel(
//            title = "Weekend Coast Ride",
//            place = "Kochi - Kanyakumari",
//            rideStatus = "Completed".uppercase(),
//            date = "Sun, Jun 22", 2
//        )
//        var hirtory1 = YourRideDataModel(
//            title = "Vagamon trip",
//            place = "Kochi - Vagamon",
//            rideStatus = "Completed".uppercase(),
//            date = "Tue, Jun 24", 2
//        )
//
//        var invites = YourRideDataModel(
//            title = "Invite from Sooraj",
//            place = "Kochi - Kanyakumari",
//            rideStatus = "",
//            date = "Tomorrow - 08:00 AM", 2
//        )
//
//        var invites1 = YourRideDataModel(
//            title = "Invite from Sreedev",
//            place = "Kochi - Wayanad",
//            rideStatus = "",
//            date = "Tomorrow - 08:00 AM", 2
//        )
//    }

}