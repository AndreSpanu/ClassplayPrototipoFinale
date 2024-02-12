package com.example.classplayprototipofinale.ui.theme.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.PopupType
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.navigation.Screen
import com.example.classplayprototipofinale.ui.theme.home.SearchBar
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.WarningType
import com.example.classplayprototipofinale.models.ToDo
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.ui.theme.BlueGradientCol
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.GridCol
import com.example.classplayprototipofinale.ui.theme.MyTypography
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.TodoBackgroundCol

enum class switcher(val bg: List<Color>, var tc: List<Color>, var w: List<Float>) {
    TODO(listOf(Color.Transparent, GridCol), listOf(GridCol, Color.White), listOf(0.9f, 1.1f)),
    DONE(listOf(GridCol, Color.Transparent), listOf(Color.White, GridCol), listOf(1.1f, 0.9f))
}

class TopBar {

    @Composable
    fun SetTopBar(currentRoute: String, sb: SearchBar, cpvm: ClassPlayViewModel, ma: MainActivity, navController: NavController) {
        var searchScreen by remember { mutableStateOf(cpvm.searchScreen.value) }
        var zoomCard by remember { mutableStateOf<Cosplay?>(null) }
        var todoCard by remember { mutableStateOf<ToDo?>(null) }
        var profileCard by remember { mutableStateOf<Users?>(null) }
        var done by remember { mutableStateOf(false) }

        cpvm.searchScreen.observe(ma) { searchScreen = it }
        cpvm.zoomCard.observe(ma) { zoomCard = it }
        cpvm.todoCard.observe(ma) { todoCard = it }
        cpvm.otherProfile.observe(ma) { profileCard = it }
        cpvm.done.observe(ma) { done = it }

        val col: switcher
        if (done) {
            col = switcher.DONE
        }
        else {
            col = switcher.TODO
        }

        /** Se faccio lo zoom di una card non mostro la TopBar
         *  Al contrario, se apro la searchBar, mostro la back arrow**/

        if (zoomCard == null && profileCard == null && currentRoute == Screen.Home.route) {
            Box(modifier = Modifier.height(80.dp)) {
                Column (
                    Modifier
                        .fillMaxSize()
                        .padding(end = 20.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center){
                    if (currentRoute == Screen.Home.route) {
                        sb.SearchTextField(cpvm = cpvm, ma)
                    }
                }
                Row (modifier = Modifier.height(80.dp), verticalAlignment = Alignment.CenterVertically){
                    if (searchScreen!!) {
                        IconButton(onClick = {
                            cpvm.setSearchScreen(false)
                            cpvm.searchFocus.value?.clearFocus()
                        }, modifier = Modifier.size(40.dp)) {
                            Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back to Home Screen", modifier = Modifier.fillMaxSize(), tint = Color.White)
                        }
                    }
                    else {
                        Column(modifier = Modifier
                            .padding(start = 15.dp)
                            .size(40.dp)
                            .background(Color.White, RoundedCornerShape(50)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Image(painter = painterResource(id = R.drawable.filter), contentDescription = "Filter the result", modifier = Modifier
                                .size(32.dp)
                                .offset(y = 4.dp)
                                .clickable {
                                    cpvm.setCardPopup(PopupType.FILTER, "")
                                })
                        }
                    }
                }
            }
        }

        else if (currentRoute == Screen.Checklist.route) {

            if (todoCard != null) {
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){

                        Button(onClick = {
                            cpvm.setDestination(null)
                            cpvm.setCardPopup(
                                PopupType.WARNING,
                                "Vuoi eliminare questa ToDo list?\n\nUna volta eliminata non sarà pù recuperabile!",
                                WarningType.ELIMINATODO
                            )
                            cpvm.setTodoEdit(cpvm.todoCard.value)
                        }, modifier = Modifier
                            .size(120.dp, 40.dp)
                            .border(2.dp, RedCol, RoundedCornerShape(50)), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = RedCol
                        )
                        ) {
                            Text(text = "Elimina", fontSize = 18.sp, style = MyTypography.typography.body2, color = RedCol)
                        }

                        Button(onClick = {
                            cpvm.setTodoEdit(cpvm.todoCard.value)
                            navController.navigate(Screen.NewTodo.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }, modifier = Modifier.size(120.dp, 40.dp), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(
                            backgroundColor = BlueGradientCol,
                            contentColor = Color.White
                        )
                        ) {
                            Text(text = "Modifica", fontSize = 17.sp, style = MyTypography.typography.body2)
                        }
                    }
                }
            }
            else {
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                    Row (modifier = Modifier
                        .width(160.dp)
                        .height(30.dp)
                        .background(TodoBackgroundCol, CircleShape)
                        .border(3.dp, BottomBarCol, CircleShape)){
                        Column(modifier = Modifier
                            .fillMaxHeight()
                            .weight(col.w[0])
                            .clickable {
                                cpvm.setDone(true)
                            }
                            .background(col.bg[0], CircleShape), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text(text = "done", fontSize = 14.sp, color = col.tc[0], style = MyTypography.typography.body1)
                        }
                        Column(modifier = Modifier
                            .fillMaxHeight()
                            .weight(col.w[1])
                            .clickable {
                                cpvm.setDone(false)
                            }
                            .background(col.bg[1], CircleShape), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text(text = "todo", fontSize = 14.sp, color = col.tc[1], style = MyTypography.typography.body1)
                        }
                    }
                }
            }
        }
    }
}