import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithListInBottomSheet(show: Boolean) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(show) }
    var text by remember { mutableStateOf("") }

    val itemsList = List(50) { "Item #$it" }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            // Sheet content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // TextField at the top
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.padding50)

                        .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                        .background(
                            NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value ="" ,
                        onValueChange = {  },
                        placeholder = {
                            Text(
                                text = stringResource(com.asphalt.commonui.R.string._search_name_number),
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

                Spacer(Modifier.height(12.dp))

                // LazyColumn below
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f) // limit height if needed
                ) {
                    items(itemsList.filter {
                        it.contains(text, ignoreCase = true)
                    }) { item ->
                        Text(
                            text = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun previewBorrom(){
    TextFieldWithListInBottomSheet(true)
}