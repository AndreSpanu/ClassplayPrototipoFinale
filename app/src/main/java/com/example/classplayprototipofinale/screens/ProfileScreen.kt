package com.example.classplayprototipofinale.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.PopupType
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.ui.theme.BlueGradientCol
import com.google.firebase.database.DatabaseReference
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.WarningType
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.navigation.Screen
import com.example.classplayprototipofinale.ui.theme.BackgroundColBlur
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.DetailsCol
import com.example.classplayprototipofinale.ui.theme.MyTypography
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.profile.ProfileCard


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(cpvm: ClassPlayViewModel, ma: MainActivity, cpDB: DatabaseReference, navController: NavController) {

    val pc = ProfileCard()

    var user by remember {  mutableStateOf(Users()) }
    var profileZoom by remember { mutableStateOf(false) }
    var favoriteOpen by remember { mutableStateOf(false) }
    var zoomCard by remember { mutableStateOf<Cosplay?>(null) }

    cpvm.favoriteOpen.observe(ma) { favoriteOpen = it }

    cpvm.currentUser.observe(ma) { user = it }
    cpvm.zoomCard.observe(ma) { zoomCard = it }


    val painter = rememberImagePainter(data = user.profileImgUrl)

    val cosplayList by remember { mutableStateOf(cpvm.cosplayList.value!!) }



    Box(Modifier.fillMaxSize()) {
        Column (
            Modifier
                .fillMaxSize()
                .padding(top = 40.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            Column (modifier = Modifier
                .size(350.dp, 580.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            BlueGradientCol,
                            BottomBarCol
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    ), RoundedCornerShape(6)
                )){
                if (favoriteOpen) {
                    Text(text = "Preferiti", fontSize = 25.sp, modifier = Modifier.padding(20.dp), style = MyTypography.typography.body2)
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(2.dp)
                        .background(Color.White))
                    FlowRow (modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .padding(horizontal = 15.dp)){
                        for (cosplay in cosplayList.filter {
                            if (it.favorites == null) {
                                false
                            }
                            else {
                                it.favorites!!.contains(cpvm.username.value)
                            }
                        }) {
                            pc.ProfileCardShow(cpvm = cpvm, cosplay = cosplay, cpDB = cpDB)
                        }
                    }
                }
                else {
                    Row (Modifier.padding(start = 20.dp, top = 15.dp)){
                        Image(painter = painter, contentDescription = "Profile icon", modifier = Modifier
                            .size(120.dp)
                            .clip(
                                RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                profileZoom = true
                            }, contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(15.dp))

                        Column {
                            cpvm.currentUser.value?.username?.let { Text(text = it, fontSize = 25.sp, style = MyTypography.typography.body2) }
                            Spacer(modifier = Modifier.height(20.dp))
                            cpvm.currentUser.value?.bio?.let { Text(text = it, fontSize = 15.sp, style = MyTypography.typography.body1) }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row (modifier = Modifier.padding(horizontal = 25.dp), verticalAlignment = Alignment.CenterVertically){
                        Icon(imageVector = Icons.Default.Call, contentDescription = "Numero di telefono", tint = Color.White)
                        Spacer(modifier = Modifier.width(15.dp))
                        cpvm.currentUser.value?.phoneNumber?.let { Text(text = it, fontSize = 20.sp, style = MyTypography.typography.body1) }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row (modifier = Modifier.padding(horizontal = 25.dp), verticalAlignment = Alignment.CenterVertically){
                        Icon(imageVector = Icons.Default.Mail, contentDescription = "Numero di telefono", tint = Color.White)
                        Spacer(modifier = Modifier.width(15.dp))
                        cpvm.currentUser.value?.emailAddress?.let { Text(text = it, fontSize = 20.sp, style = MyTypography.typography.body1) }
                    }

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .padding(top = 20.dp)
                        .height(2.dp)
                        .background(Color.White))

                    FlowRow (modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .verticalScroll(rememberScrollState()), horizontalArrangement = Arrangement.SpaceBetween){
                        for (cosplay in cosplayList.filter { it.username == cpvm.username.value }) {
                            pc.ProfileCardShow(cpvm = cpvm, cosplay = cosplay, cpDB = cpDB)
                        }
                    }
                }
            }
        }

        /** Topbar: Preferiti, Modifica e Impostazioni **/

        if (zoomCard == null) {
            Row (
                Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                if (!favoriteOpen) {
                    Icon(painter = painterResource(id = R.drawable.heart), contentDescription = "Preferiti", modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            cpvm.setFavoriteOpen(true)
                        }, tint = DetailsCol)
                }
                else {
                    Image(painter = painter, contentDescription = "Profile icon", modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            cpvm.setFavoriteOpen(false)
                        }
                        .align(Alignment.CenterVertically)
                        .clip(
                            RoundedCornerShape(50.dp)
                        ), contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier
                    .size(40.dp)
                    .background(DetailsCol, CircleShape), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(painter = painterResource(id = R.drawable.pencil), contentDescription = "Modifica profilo", modifier = Modifier
                        .size(23.dp)
                        .clickable {
                            navController.navigate(Screen.ProfileEdit.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true


                                cpvm.setNewProfileEdit(user)
                                cpvm.cFormTitle(user.username!!)
                                cpvm.addImgForm(
                                    user.profileImgUrl!!,
                                    cpvm.username.value!! + "Edit"
                                )
                                user.bio?.let { cpvm.newDescriptionForm(it) }
                                user.phoneNumber?.let { cpvm.compileTag(it) }
                                user.emailAddress?.let { cpvm.newMaterialDescriptionCosplayForm(it) }
                            }
                        }, tint = Color.White)
                }

                Icon(painter = painterResource(id = R.drawable.settings), contentDescription = "Impostazioni", modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        cpvm.setCardPopup(PopupType.IMPOSTAZIONI, "")
                    }, tint = DetailsCol)
            }

            /** Lo zoom dell'immagine profilo **/

            if (profileZoom) {
                Column (modifier = Modifier
                    .fillMaxSize()
                    .clickable { }
                    .background(BackgroundColBlur), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    Box (modifier = Modifier.size(300.dp, 450.dp)){
                        Image(painter = painter, contentDescription = "Immagine del profilo", modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(6)
                            ), contentScale = ContentScale.Crop)

                        Row (modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 7.dp, end = 7.dp), horizontalArrangement = Arrangement.End){
                            Column(modifier = Modifier
                                .size(28.dp)
                                .background(Color.White, RoundedCornerShape(50))
                                .clickable {
                                    profileZoom = false
                                }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Chiudi immagine", modifier = Modifier.size(26.dp))
                            }
                        }
                    }
                }
            }
        }

        if (!favoriteOpen && zoomCard != null) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){

                Button(onClick = {
                    cpvm.setCardPopup(PopupType.WARNING, "Sei sicuro di voler eliminare questo Cosplay?\n\nUna volta eliminato non potrà essere recuperato!", WarningType.ELIMINACOSPLAY)
                    cpvm.setDestination(Screen.Profile.route)
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
                    cpvm.setDestination(Screen.NewCosplay.route)
                    cpvm.setCardPopup(PopupType.WARNING, "\n\nVuoi modificare questo Cosplay?", WarningType.MODIFICACOSPLAY)
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
}