package com.asphalt.login


import android.provider.CalendarContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asphalt.commonui.Theme.Dimensions
import java.nio.file.WatchEvent

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(Dimensions.padding85))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(text = "Welcome", fontSize = 22.sp, color = Color.Black)
            Text(
                text = "Let’s login for explore continues",
                modifier = Modifier.padding(top = Dimensions.padding15),
                fontSize = 15.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(Dimensions.spacing20))
            Box(
                modifier = Modifier
                    .height(Dimensions.padding100)
                    .width(Dimensions.padding100)
                    .background(Color.Red)
            )
            //Image(painter = painterResource(R.drawable.),contentDescription = null)
            Text(text = "adesso Riders's Club", modifier = Modifier.padding(top = 20.dp))
        }
//========
        var text1 by remember { mutableStateOf("") }
        var text2 by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimensions.padding30, end = Dimensions.padding30)
                .weight(1f),
        ) {
            Text(text = "Email or Phone Number", modifier = Modifier.padding(top = 20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.padding50)
                    .background(
                        Color(0xFFF8F7FB),
                        shape = RoundedCornerShape(Dimensions.padding10)
                    )
                    .border(
                        width = Dimensions.padding1,
                        color = Color(0xFFF8F7FB),
                        shape = RoundedCornerShape(Dimensions.padding10)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = text1,
                    onValueChange = { text1 = it },
                    //label = { Text("Enter your email") },
                    placeholder = { Text("Enter your email", style = TextStyle(fontSize = 16.sp)) },
                    textStyle = TextStyle(fontSize = 16.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email, // Specify the icon (e.g., Email)
                            contentDescription = "Email icon",
                            tint = Color.Gray // Set the icon color
                        )
                    }
                )

                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                        .background(Color.Red)
                )
                //Image()
            }
            Text(text = "Password", modifier = Modifier.padding(top = 20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.padding50)
                    .background(
                        Color(0xFFF8F7FB),
                        shape = RoundedCornerShape(Dimensions.padding10)
                    )
                    .border(
                        width = Dimensions.padding1,
                        color = Color(0xFFF8F7FB),
                        shape = RoundedCornerShape(Dimensions.padding10)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = text2,
                    onValueChange = { text2 = it },
                    placeholder = {
                        Text(
                            "Enter your password",
                            style = TextStyle(fontSize = 16.sp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxHeight(),
                    textStyle = TextStyle(fontSize = 16.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email, // Specify the icon (e.g., Email)
                            contentDescription = "Email icon",
                            tint = Color.Gray // Set the icon color
                        )
                    }
                )
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                        .background(Color.Red)
                )
                //Image()
            }

        }
        //===
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(text = "Welcome", fontSize = 22.sp, color = Color.Black)
            Text(
                text = "Let’s login for explore continues",
                modifier = Modifier.padding(top = 15.dp), fontSize = 15.sp, color = Color.Gray
            )
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .background(Color.Red)
            )
            //Image(painter = painterResource(R.drawable.),contentDescription = null)
            Text(text = "adesso Riders's Club", modifier = Modifier.padding(top = 20.dp))
        }
    }


}

@Preview
@Composable
fun LoginPreview() {
    LoginScreen()
}

