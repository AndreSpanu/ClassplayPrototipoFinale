package com.example.classplayprototipofinale.ui.theme.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.PopupType
import com.example.classplayprototipofinale.WarningType
import com.example.classplayprototipofinale.ui.theme.GridCol
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.WarningCol
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference


class WarningUp {
    @Composable
    fun Warning(cpvm: ClassPlayViewModel, navController: NavController, cpDB: DatabaseReference, uDB: DatabaseReference, cosplaysImgsSRef: StorageReference, profileIconsSRef: StorageReference) {

        val message by remember { mutableStateOf(cpvm.cardPopup.value!!.second) }
        val warningType by remember { mutableStateOf(cpvm.cardPopup.value!!.third) }

        val size : Size = if (warningType == WarningType.TODOSTEPMANCANTI || warningType == WarningType.COSPLAYSTEPMANCANTI)
            Size(280f, 310f)
        else
            Size(270f, 200f)


        Column (modifier = Modifier.fillMaxSize().clickable {  }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Column (modifier = Modifier
                .size(size.width.dp, size.height.dp)
                .background(WarningCol, RoundedCornerShape(10))
                .padding(vertical = 10.dp, horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween){

                Text(text = message, fontSize = 20.sp, color = Color.White, textAlign = TextAlign.Center)

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween){
                    Column (modifier = Modifier
                        .width(80.dp)
                        .clickable {
                            cpvm.setCardPopup(PopupType.NONE)
                        }
                        .padding(vertical = 6.dp)
                        .background(Color.White, RoundedCornerShape(50))
                        .border(3.dp, RedCol, RoundedCornerShape(50)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center){
                        Text(text = "No", color = RedCol, fontSize = 22.sp)
                    }

                    Column (modifier = Modifier
                        .width(80.dp)
                        .clickable {

                            cpvm.setCardPopup(PopupType.NONE)

                            when (warningType) {

                                WarningType.MODIFICACOSPLAY -> {
                                    cpvm.cosplayEditSet()
                                }

                                WarningType.ELIMINACOSPLAY -> {
                                    if (cpvm.zoomCard.value != null)
                                        cpDB.child(cpvm.zoomCard.value!!.cosplayName!!).removeValue()
                                    cpvm.setZoomCard(null)
                                }

                                WarningType.ELIMINATODO -> {
                                    uDB.child(cpvm.username.value!!).child("yourToDo").child(cpvm.todoEdit.value?.todoTitle.toString()).removeValue()
                                }

                                WarningType.TODODACOSPLAY -> {
                                    cpvm.newTodoFromStep()
                                }

                                WarningType.TODOSTEPMANCANTI -> {
                                    cpvm.saveTodoWarning(uDB, cosplaysImgsSRef)
                                }

                                WarningType.COSPLAYSTEPMANCANTI -> {
                                    cpvm.saveCosplayWarning(cpDB, cosplaysImgsSRef)
                                }

                                WarningType.ANNULLA -> {
                                    cpvm.annulla(cosplaysImgsSRef = cosplaysImgsSRef, profileIconsSRef)
                                }

                                WarningType.MODIFICAPROFILO -> {
                                    cpvm.saveProfile(uDB, profileIconsSRef)
                                }

                                else -> {}
                            }

                            if (cpvm.destination.value != null) {
                                navController.navigate(cpvm.destination.value!!) {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                        }
                        .padding(vertical = 6.dp)
                        .background(GridCol, RoundedCornerShape(50)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center){
                        Text(text = "Si", color = Color.White, fontSize = 22.sp)
                    }
                }
            }
        }
    }
}