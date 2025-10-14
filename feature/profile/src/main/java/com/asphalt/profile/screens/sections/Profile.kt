package com.asphalt.profile.screens.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.model.CurrentUser
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.PrimaryBrighterLightB33
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.profile.viewmodels.ProfileSectionVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSection(
    user: CurrentUser?,
    profileSectionVM: ProfileSectionVM = koinViewModel(),
) {
    LaunchedEffect(user) {
        profileSectionVM.getProfileData(user?.uid)
    }
    val profileData = profileSectionVM.profileData.collectAsStateWithLifecycle()
    ComposeUtils.CommonContentBox(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = Dimensions.padding30,
                    horizontal = Dimensions.padding20
                ),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing20),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedBox(
                modifier = Modifier.size(Dimensions.size81),
                borderStroke = Dimensions.size4,
                borderColor = PrimaryBrighterLightB33, cornerRadius = Dimensions.size81
            ) {

                CircularNetworkImage(
                    modifier = Modifier
                        .align(Alignment.Center),
                    imageUrl = profileData.value?.profilePicUrl ?: ""
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(Dimensions.spacing5)) {
                Text(
                    profileData.value?.username ?: "", style = TypographyBold.bodyMedium,
                    fontSize = Dimensions.textSize19
                )
                Text(
                    profileData.value?.userEmail ?: "",
                    style = Typography.bodyMedium,
                    fontSize = Dimensions.textSize16
                )

                Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing15)) {
                    if (profileData.value?.isMechanic == true)
                        ProfileLabel(R.drawable.ic_spanner, "Mechanic")
                    ProfileLabel(R.drawable.ic_call, profileData.value?.phoneNumber ?: "")

                }
            }
        }
    }
}

@Composable
fun ProfileLabel(drawable: Int, text: String) {
    RoundedBox(cornerRadius = Dimensions.size5) {
        Row(
            modifier = Modifier.padding(
                horizontal = Dimensions.padding8,
                vertical = Dimensions.padding5
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                Dimensions.spacing5
            )
        ) {
            Image(
                painter = painterResource(drawable),
                null
            )
            Text(text, style = Typography.bodyMedium, fontSize = Dimensions.textSize12)

        }
    }
}