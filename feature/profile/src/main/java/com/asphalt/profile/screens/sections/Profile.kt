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
import com.asphalt.android.viewmodels.AndroidUserVM
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
    profileSectionVM: ProfileSectionVM = koinViewModel(),
    androidUserVM: AndroidUserVM = koinViewModel()
) {
    val user = androidUserVM.userState.collectAsStateWithLifecycle()
    LaunchedEffect(user) {
        profileSectionVM.getProfileData(user.value?.uid)
    }
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
                        .size(Dimensions.size73)
                        .align(Alignment.Center),
                    imageUrl = "https://picsum.photos/id/1/200/300"
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(Dimensions.spacing5)) {
                Text(
                    user.value?.name.toString(), style = TypographyBold.bodyMedium,
                    fontSize = Dimensions.textSize19
                )
                Text(
                    user.value?.email.toString(),
                    style = Typography.bodyMedium,
                    fontSize = Dimensions.textSize16
                )
//                        Spacer(Modifier.height(Dimensions.padding5))

                Row(horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing15)) {

                    ProfileLabel(R.drawable.ic_spanner, "Mechanic")
                    ProfileLabel(R.drawable.ic_call, "999999999")

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