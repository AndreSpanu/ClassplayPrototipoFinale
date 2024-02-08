package com.example.classplayprototipofinale.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Checklist : Screen("checklist_screen")
    object Chat : Screen("chat_screen")
    object Profile : Screen("profile_screen")
    object ProfileEdit : Screen("profile_edit_screen")
    object NewCosplay : Screen("cosplayform_screen")
    object NewTodo : Screen("todoform_screen")
}