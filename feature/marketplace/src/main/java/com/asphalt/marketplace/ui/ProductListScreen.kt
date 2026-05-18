package com.asphalt.marketplace.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography
import com.asphalt.marketplace.ui.composable.MainTabs
import com.asphalt.marketplace.ui.composable.SubTabs
import com.asphalt.marketplace.viewmodel.ProductListViewModel
import org.koin.compose.koinInject

@Composable
fun ProductListScreen(
    setTopAppBarState: (AppBarState) -> Unit,
    productListViewmodel: ProductListViewModel = koinInject()
) {
    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.marketplace)
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NeutralWhite)
    ) {
        MainTabs(productListViewmodel)
        Spacer(modifier = Modifier.height(Dimensions.size20))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                SubTabs()
            }
        }

    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ProductListPreview() {
    ProductListScreen({}, ProductListViewModel())
}