package com.example.myapplication


sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Item : Screen("item/{furniId}") {
        fun createRoute(furniId: Int) = "item/$furniId"
    }
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    object About: Screen("about")
}