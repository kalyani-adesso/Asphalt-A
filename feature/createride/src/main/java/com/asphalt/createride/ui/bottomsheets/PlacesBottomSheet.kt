import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.BouncingCirclesLoader
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded
import com.asphalt.createride.viewmodel.PlacesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesBottomSheet(show: Boolean, placesVM: PlacesViewModel = koinViewModel()) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(show) }
    var text by remember { mutableStateOf("") }


    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            // Sheet content
            BottomSheetLayout(placesVM)
        }
    }
}

@Composable
fun BottomSheetLayout(placesVM: PlacesViewModel?) {
    if (placesVM?._showLoader?.value ?: false) {
        BouncingCirclesLoader()
    }

    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.padding50)
                .background(
                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = stringResource(com.asphalt.commonui.R.string.search_location),
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,


                        )
                },
                textStyle = Typography.bodyMedium.copy(NeutralDarkGrey),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    ),
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search_blue),
                        contentDescription = "Email icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.clickable {
                            placesVM?.getPlaces(text)
                        }

                    )
                }

            )

        }

        Spacer(Modifier.height(12.dp))

        // LazyColumn below
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f) // limit height if needed
        ) {
            items(placesVM?.placeData?.value ?: emptyList()) { item ->
                Box(
                    modifier = Modifier.padding(
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = NeutralWhite,
                                shape = RoundedCornerShape(Dimensions.size10)
                            )
                            .padding(horizontal = Dimensions.padding16, vertical = Dimensions.padding15),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(modifier = Modifier.fillMaxWidth()) {
                            ColorIconRounded(
                                backColor = GreenLIGHT,
                                resId = R.drawable.ic_route_white
                            )
                            Spacer(modifier = Modifier.width(Dimensions.size8))
                            Column {
                                Text(
                                    text = item.name ?: "",
                                    style = TypographyMedium.titleMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.height(Dimensions.size3))
                                Text(
                                    text = item.displayName ?: "",
                                    style = Typography.bodySmall,
                                    color = NeutralDarkGrey
                                )
                            }

                        }


                    }
                }
                Spacer(modifier = Modifier.height(Dimensions.size10))
            }
        }
    }
}


@Preview
@Composable
fun previewBorrom() {
    BottomSheetLayout(null)
}