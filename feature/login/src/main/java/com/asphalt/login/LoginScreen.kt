package com.asphalt.login


import android.provider.CalendarContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.nio.file.WatchEvent

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(85.dp))
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
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
//========
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
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
        //===
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
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

