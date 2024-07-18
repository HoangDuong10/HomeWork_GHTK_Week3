package com.example.profile.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults.IconSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.profile.R
import com.example.profile.model.BottomBarScreen
import com.example.profile.ui.theme.ProfileTheme

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val items = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Settings,
        BottomBarScreen.Profile,
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(0.3.dp, color = colorResource(id = R.color.grey))), // Set border on top
                    ) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any{
                            it.route== item.route
                        } == true,
                        onClick = {
                            navController.navigate(item.route){
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                painterResource(id = item.icon) ,
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp)

                            )
                        },
                        label = {
                            Text(text = item.title)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorResource(id = R.color.green),
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = colorResource(id = R.color.green),
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.White

                        ),

                    )
                }
            }
        },
    ) {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            BottomNavGraph(navController = navController)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(navController : NavHostController){
    NavHost(navController = navController,
        startDestination = BottomBarScreen.Home.route
    ){
        composable(route = BottomBarScreen.Home.route){
            CountCharactersScreen()
        }

        composable(route = BottomBarScreen.Settings.route){
            TriangleCheckScreen()
        }

        composable(route = BottomBarScreen.Profile.route){
            ProfileScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainPreview(){
    ProfileTheme {
        ProfileScreen()
    }
}