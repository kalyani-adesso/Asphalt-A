package com.asphalt.marketplace.ui

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextOverflow.Companion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.size.Dimension
import com.asphalt.android.model.chat.getOtherUserId
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GrayLite33
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.CircularNetworkImage
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.marketplace.ui.composable.MainTabs
import com.asphalt.marketplace.ui.composable.SubTabs
import com.asphalt.marketplace.viewmodel.ProductListViewModel
import io.ktor.http.ContentType
import org.koin.compose.koinInject
import java.nio.file.WatchEvent

@Composable
fun ProductListScreen(
    setTopAppBarState: (AppBarState) -> Unit,
    productListViewmodel: ProductListViewModel = koinInject(), postAddClick: () -> Unit,
    productClick: () -> Unit
) {
    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.marketplace),
            actions = {
                RoundedBox(
                    borderColor = PrimaryDarkerLightB75,
                    borderStroke = Dimensions.padding1,
                    cornerRadius = Dimensions.size10,
                    modifier = Modifier
                        .padding(end = Dimensions.padding15)
                        .clickable {
                            postAddClick.invoke()
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = Dimensions.size10)
                            .height(Dimensions.padding30),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            tint = PrimaryDarkerLightB75,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(Dimensions.spacing5))
                        Text(
                            stringResource(R.string.post_ad),
                            color = PrimaryDarkerLightB75,
                            fontSize = Dimensions.textSize12,
                            style = TypographyBold.titleMedium
                        )
                    }
                }
            }
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NeutralWhite)
    ) {
        MainTabs(productListViewmodel)
        Spacer(modifier = Modifier.height(Dimensions.size20))
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.padding50)

                        .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                        .background(
                            NeutralLightPaper, shape = RoundedCornerShape(Dimensions.padding10)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = "",
                        onValueChange = { },
                        placeholder = {
                            Text(
                                text = "Search items",//stringResource(R.string._search_name_number),
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
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_search_blue),
                                contentDescription = "Email icon",
                                tint = Color.Unspecified

                            )
                        }

                    )

                }
            }

            item {
                Spacer(modifier = Modifier.height(Dimensions.size20))
                SubTabs(productListViewmodel)
            }

        }
        Spacer(modifier = Modifier.height(Dimensions.size20))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimensions.padding16)
                .clip(RoundedCornerShape(16.dp))
                .background(NeutralLightPaper)
        ) {
            items(10) {
                Spacer(modifier = Modifier.height(Dimensions.size16))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(horizontal = Dimensions.padding16)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GrayLite33)
                        .testTag("product_item")
                        .clickable {
                            productClick.invoke()
                        }

                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = Dimensions.padding16),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = R.drawable.naked_bike,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(97.dp)
                                    .height(95.dp)
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Column(modifier = Modifier.padding(horizontal = Dimensions.size8)) {
                            Spacer(modifier = Modifier.height(Dimensions.padding16))
                            Text(text = "Yahama R15 2020", style = TypographyBold.bodyLarge)
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_favorite_icon_gray),
                                    contentDescription = ""
                                )
                            }
                            Text(text = "Single owner", style = Typography.bodySmall)
                            Spacer(modifier = Modifier.height(Dimensions.padding16))
                            Text(
                                text = "₹ 50000",
                                style = TypographyBold.bodyLarge,
                                color = PrimaryDarkerLightB75
                            )
                            Spacer(modifier = Modifier.height(Dimensions.padding16))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularNetworkImage(
                                        modifier = Modifier.border(
                                            width = Dimensions.size2pt5,
                                            color =
                                                NeutralWhite,
                                            shape = CircleShape
                                        ),
                                        size = Dimensions.size20,
                                        imageUrl = ""
                                    )
                                    Spacer(modifier = Modifier.width(Dimensions.size5))
                                    Text(
                                        text = "Hari", style = Typography.bodyMedium,
                                        maxLines = 1, overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Text("2 days ago", style = Typography.bodyMedium)


                            }
                            Spacer(modifier = Modifier.height(Dimensions.padding16))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_location),
                                    contentDescription = "",
                                    colorFilter = ColorFilter.tint(PrimaryDarkerLightB75)
                                )
                                Spacer(modifier = Modifier.width(Dimensions.padding8))
                                Text("Kakkanad,Kochi", style = Typography.bodyMedium)
                            }
                            Spacer(modifier = Modifier.height(Dimensions.padding16))
                        }
                    }

                }
                //Spacer(modifier = Modifier.height(Dimensions.size10))
            }
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ProductListPreview() {
    ProductListScreen({}, ProductListViewModel(), {}, {})
}