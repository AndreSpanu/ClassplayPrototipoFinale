package com.example.classplayprototipofinale.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.screens.ChatScreen
import com.example.classplayprototipofinale.screens.CheckListScreen
import com.example.classplayprototipofinale.screens.CosplayFormScreen
import com.example.classplayprototipofinale.screens.HomeScreen
import com.example.classplayprototipofinale.screens.ProfileEdit
import com.example.classplayprototipofinale.screens.ProfileScreen
import com.example.classplayprototipofinale.screens.TodoFormScreen
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference


@Composable
fun NavigationSetup(navController: NavHostController, cpvm: ClassPlayViewModel, ma: MainActivity, cpDB: DatabaseReference, profileIconsSRef: StorageReference, uDB: DatabaseReference, cosplayImgsRef: StorageReference, user: Users) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Chat.route) {
            ChatScreen(cpvm = cpvm)
        }
        composable(BottomNavItem.Home.route) {
            HomeScreen(cpvm, ma, cpDB, uDB, user)
        }
        composable(BottomNavItem.Checklist.route) {
            CheckListScreen(navController, cpvm, ma, uDB = uDB, user)
        }
        composable(Screen.Profile.route) {
            ProfileScreen( cpvm = cpvm, ma = ma, cpDB = cpDB, navController)
        }
        composable(BottomNavItem.CosplayForm.route) {
            CosplayFormScreen(navController = navController, cpvm = cpvm, ma = ma, cpDB = cpDB, cosplaysImgsSRef = cosplayImgsRef)
        }
        composable(Screen.NewTodo.route) {
            TodoFormScreen(navController = navController, cpvm, ma, cosplayImgsRef, uDB)
        }
        composable(Screen.ProfileEdit.route) {
            ProfileEdit(cpvm, uDB, ma, navController, profileIconsSRef)
        }

    }
}