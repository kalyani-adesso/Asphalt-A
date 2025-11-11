package com.asphalt.profile.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.commonui.CombinedCameraGalleryLauncher
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.MagentaDeep
import com.asphalt.commonui.theme.PrimaryBrighterLightB33
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.ScarletRed
import com.asphalt.commonui.theme.VividOrange
import com.asphalt.commonui.ui.BorderedButton
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.ComposeUtils.CustomSwitch
import com.asphalt.commonui.utils.ComposeUtils.HeaderWithInputField
import com.asphalt.profile.viewmodels.EditProfileVM
import com.asphalt.profile.viewmodels.ProfileSectionVM
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun EditProfile(
    onSaveChanges: (String, String, String, String, String, Boolean) -> Unit,
    onDismiss: () -> Unit,
    editProfileVM: EditProfileVM = koinViewModel(),
    profileSectionVM: ProfileSectionVM = koinActivityViewModel()
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val scope = rememberCoroutineScope()
        val profileData = profileSectionVM.profileData.collectAsStateWithLifecycle()
        val fullName = editProfileVM.editFullName.collectAsStateWithLifecycle()
        val email = editProfileVM.editEmail.collectAsStateWithLifecycle()
        val phoneNumber = editProfileVM.editPhoneNumber.collectAsStateWithLifecycle()
        val license = editProfileVM.editLicense.collectAsStateWithLifecycle()
        val mechanic = editProfileVM.editMechanic.collectAsStateWithLifecycle()
        val emergencyNo = editProfileVM.editEmergencyNo.collectAsStateWithLifecycle()
        val fullNameError = editProfileVM.fullNameError.collectAsStateWithLifecycle()
        val emailError = editProfileVM.emailError.collectAsStateWithLifecycle()
        val phoneNumberError = editProfileVM.phoneNumberError.collectAsStateWithLifecycle()
        val licenseError = editProfileVM.licenseError.collectAsStateWithLifecycle()
        val emergencyNoError = editProfileVM.emergencyNoError.collectAsStateWithLifecycle()
        var imageUrl by remember { mutableStateOf(profileData.value?.profilePicUrl) }

        LaunchedEffect(profileData.value) {
            imageUrl = profileData.value?.profilePicUrl
            editProfileVM.setCurrentProfile(profileData.value)
        }
        var launchPicker: (() -> Unit)? by remember { mutableStateOf(null) }
        CombinedCameraGalleryLauncher(onMediaPicked = { url ->
            url?.let {
                imageUrl = it.toString()
            }
        }, trigger = { launch ->
            launchPicker = launch
        })

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = { onDismiss() })
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopStart
        ) {
            ComposeUtils.CommonContentBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Dimensions.padding50,
                        start = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                        end = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING
                    )
                    .clickable(enabled = false) {}
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                        vertical = Dimensions.size25
                    ), verticalArrangement = Arrangement.spacedBy(Dimensions.spacing20)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ComposeUtils.RoundedIconWithHeaderComponent(
                            VividOrange,
                            R.drawable.ic_edit,
                            stringResource(R.string.edit_profile),
                            stringResource(R.string.update_profile_desc)
                        )

                        Image(
                            painter = painterResource(R.drawable.ic_close),
                            null,
                            modifier = Modifier
                                .clickable {
                                    onDismiss.invoke()
                                }
                        )
                    }
                    Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        RoundedBox(
                            modifier = Modifier
                                .size(Dimensions.size81),
                            borderStroke = Dimensions.size4,
                            borderColor = PrimaryBrighterLightB33, cornerRadius = Dimensions.size81
                        ) {

                            CircularNetworkImage(
                                modifier = Modifier
                                    .align(Alignment.Center),
                                imageUrl = imageUrl.orEmpty()
                            )
                        }
                        ComposeUtils.ColorIconRounded(
                            size = Dimensions.size38,
                            resId = R.drawable.ic_photo_camera,
                            backColor = PrimaryBrighterLightW75,
                            radius = Dimensions.size38,
                            modifier = Modifier
                                .align(
                                    Alignment.BottomEnd
                                )
                                .offset(Dimensions.spacing15, y = Dimensions.spacingNeg8)
                                .clickable(enabled = launchPicker != null) {
                                    scope.launch {
                                        launchPicker?.invoke()
                                    }
                                }
                        )
                    }

                    HeaderWithInputField(
                        stringResource(R.string.full_name),
                        fullName.value,
                        {
                            editProfileVM.updateFullName(it)
                        },
                        stringResource(R.string.enter_name),
                        fullNameError.value,
                        stringResource(R.string.full_name_cannot_be_empty)
                    )

                    HeaderWithInputField(
                        stringResource(R.string.email),
                        email.value,
                        {
                            editProfileVM.updateEmail(it)
                        },
                        stringResource(R.string.enter_email_placeholder),
                        emailError.value,
                        stringResource(R.string.enter_valid_email), readOnly = true
                    )

                    HeaderWithInputField(
                        stringResource(R.string.phone_number),
                        phoneNumber.value,
                        {
                            editProfileVM.updatePhoneNumber(it)
                        },
                        stringResource(R.string.enter_phone_number),
                        phoneNumberError.value,
                        stringResource(R.string.phone_number_error)
                    )

                    HeaderWithInputField(
                        stringResource(R.string.driving_license_number),
                        license.value,
                        {
                            editProfileVM.updateLicense(it)
                        },
                        stringResource(R.string.enter_license_number),
                        licenseError.value,
                        stringResource(R.string.license_number_error)
                    )

                    HeaderWithInputField(
                        stringResource(R.string.emergency_contact_number),
                        emergencyNo.value,
                        {
                            editProfileVM.updateEmergencyNo(it)
                        },
                        stringResource(R.string.enter_emergency_number),
                        emergencyNoError.value,
                        stringResource(R.string.emergency_contact_cannot_be_empty)
                    )
                    RoundedBox(
                        modifier = Modifier.height(Dimensions.size60),
                        cornerRadius = Dimensions.size10, contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimensions.padding15),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            ComposeUtils.RoundedIconWithHeaderComponent(
                                MagentaDeep,
                                R.drawable.ic_spanner,
                                stringResource(R.string.mark_as_mechanic),
                                stringResource(R.string.your_response_will_be_highlighted),
                                titleColor = MagentaDeep
                            )
                            CustomSwitch(mechanic.value) {
                                editProfileVM.updateMechanic(it)
                            }


                        }

                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing20)
                    ) {
                        BorderedButton(
                            onClick = {
                                onDismiss.invoke()
                            },
                            borderColor = ScarletRed,
                            modifier = Modifier.weight(1f),
                            contentPaddingValues = PaddingValues(
                                Dimensions.size0
                            )
                        ) {
                            ComposeUtils.DefaultButtonContent(
                                stringResource(R.string.cancel).uppercase(),
                                color = ScarletRed
                            )
                        }
                        GradientButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                scope.launch {
                                    if (editProfileVM.validateProfileFields()) {
                                        onSaveChanges(
                                            fullName.value,
                                            email.value,
                                            phoneNumber.value,
                                            license.value,
                                            emergencyNo.value,
                                            mechanic.value
                                        )
                                        onDismiss.invoke()
                                    }
                                }
                            },
                            contentPadding = PaddingValues(
                                Dimensions.size0
                            )
                        ) {
                            ComposeUtils.DefaultButtonContent(stringResource(R.string.save_changes).uppercase())
                        }
                    }


                }
            }
        }
    }
}



