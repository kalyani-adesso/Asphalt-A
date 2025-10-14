package com.asphalt.android.navigation

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.asphalt.commonui.R

@Composable
fun BotttomNavBar(navController: NavController) {

    val items = listOf(
        NavKey.DashboardNavKey to R.drawable.ic_home,
        NavKey.RidersKey to R.drawable.ic_rides,
        NavKey.QueriesKey to R.drawable.ic_queries,
        NavKey.ProfileKey to R.drawable.ic_profile
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar{


        NavigationBarItem(
            selected = false,
            onClick = {  },
            label = { Text(text = "Rides") },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_rides),
                    contentDescription = "")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {  },
            label = { Text(text = "Queries") },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_queries),
                    contentDescription = "")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {  },
            label = { Text(text = "Profile") },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "")
            }
        )
    }
}

@Preview
@Composable
fun BotttomNavBarPreview() {

    MaterialTheme {
       // BotttomNavBar()
    }
}


enum class BottomTab(val title: String) {
    Dashboard("home"),
    Rides("rides"),
    Queries("queries"),
    Profile("profile")
}

