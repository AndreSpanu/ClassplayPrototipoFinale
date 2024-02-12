package com.example.classplayprototipofinale.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.PopupType
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.WarningType
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.google.firebase.storage.StorageReference

sealed class BottomNavItem(
    val route: String,
    @StringRes val titleResId: Int,
    val icon: Int
) {
    object Home : BottomNavItem(
        route = Screen.Home.route,
        titleResId = R.string.screen_title_home,
        icon = R.drawable.home_icon
    )

    object Checklist : BottomNavItem(
        route = Screen.Checklist.route,
        titleResId = R.string.screen_title_checklist,
        icon = R.drawable.todo_icon
    )

    object Chat : BottomNavItem(
        route = Screen.Chat.route,
        titleResId = R.string.screen_title_chat,
        icon = R.drawable.chat_icon
    )

    object CosplayForm : BottomNavItem(
        route = Screen.NewCosplay.route,
        titleResId = R.string.screen_title_cosplayForm,
        icon = R.drawable.cosplayform_icon
    )

    object Profile : BottomNavItem(
        route = Screen.Profile.route,
        titleResId = R.string.screen_title_cosplayForm,
        icon = R.drawable.cosplayform_icon
    )
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    cpvm: ClassPlayViewModel,
    painter: Painter,
    cosplayImgsSRef:StorageReference
) {
    val items = listOf(
        BottomNavItem.Chat,
        BottomNavItem.Home,
        BottomNavItem.CosplayForm,
        BottomNavItem.Checklist,
        BottomNavItem.Profile
    )

    BottomNavigation  (backgroundColor = Color.Transparent, elevation = 0.dp, modifier = Modifier
        .padding(15.dp, 0.dp, 15.dp, 15.dp)
        .background(
            BottomBarCol, RoundedCornerShape(50)
        )){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    if (item.route == Screen.Profile.route) {
                        Image(painter = painter, contentDescription = "Profile icon", modifier = Modifier
                            .size(42.dp)
                            .align(CenterVertically)
                            .clip(
                                RoundedCornerShape(50.dp)
                            ), contentScale = ContentScale.Crop
                        )
                    }
                    else {
                        Icon(painter = painterResource(id = item.icon),
                            contentDescription = stringResource(id = item.titleResId),
                            modifier = Modifier.size(40.dp),
                            tint = Color.White)
                    }
                },
                selected = currentRoute == item.route,
                onClick = {
                    cpvm.setZoomCard(null)
                    cpvm.setOtherProfile(null)
                    cpvm.setStepVideo(null)
                    cpvm.setTodoStepVideo(null)
                    cpvm.setTodoCard(null)

                    if (currentRoute == BottomNavItem.CosplayForm.route || currentRoute == Screen.NewTodo.route || currentRoute == Screen.ProfileEdit.route) {
                        cpvm.setCardPopup(PopupType.WARNING, "Vuoi abbandonare la pagina?\n\nLe modifiche andranno perse!", WarningType.ANNULLA)
                        cpvm.setDestination(item.route)
                    }
                    else {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                modifier = Modifier.background(color= Color.Transparent),
            )
        }
    }
}