package com.example.profile.model

import com.example.profile.R


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
){
    data object Home : BottomBarScreen(
        route = "Count Characters",
        title = "Count Characters",
        icon  = R.drawable.ic_count_characters
    )

    data object Settings : BottomBarScreen(
        route = "Triangle Check",
        title = "Triangle Check",
        icon = R.drawable.ic_triangle
    )

    data object Profile : BottomBarScreen(
        route = "Profile",
        title = "Profile",
        icon = R.drawable.ic_person
    )
}