package com.example.classplayprototipofinale.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.PopupType
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.ui.theme.BlueGradientCol
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.WarningType
import com.example.classplayprototipofinale.navigation.Screen
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.DetailsCol
import com.example.classplayprototipofinale.ui.theme.MyTypography
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.form.CheckForm
import com.example.classplayprototipofinale.ui.theme.form.ProfileTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileEdit(cpvm: ClassPlayViewModel, uDB: DatabaseReference, ma: MainActivity, navController: NavController, profileIconsSRef: StorageReference) {

    val pt = ProfileTextField()
    val cf = CheckForm()

    var user by remember {  mutableStateOf(Users()) }

    var data by remember { mutableStateOf(user.profileImgUrl) }

    cpvm.profileEdit.observe(ma) {
        if (it != null) {
            user = it
            data = user.profileImgUrl
        }
    }

    val painter = rememberImagePainter(data = data)

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
                )
                .padding(20.dp)
                .verticalScroll(rememberScrollState())){

                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Box(modifier = Modifier.size(260.dp)) {
                        Image(painter = painter, contentDescription = "Immagine del profilo", modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10)),
                            contentScale = ContentScale.Crop)

                        Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                            Column (modifier = Modifier
                                .size(50.dp)
                                .background(DetailsCol, CircleShape)
                                .clickable {
                                    ma.uploadImage("profileIcon")
                                }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                                Image(painter = painterResource(id = R.drawable.pencil), contentDescription = "Modifica la foto del profilo", modifier = Modifier
                                    .size(35.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Username", fontSize = 18.sp, style = MyTypography.typography.body2)

                pt.UsernameTextField(cpvm = cpvm)

                Text(text = "Bio", fontSize = 18.sp, style = MyTypography.typography.body2)

                pt.BioTextField(cpvm = cpvm)
                
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Telefono", fontSize = 18.sp, style = MyTypography.typography.body2)

                pt.PhoneTextField(cpvm = cpvm)

                Text(text = "Email", fontSize = 18.sp, style = MyTypography.typography.body2)

                Spacer(modifier = Modifier.height(10.dp))

                pt.EmailTextField(cpvm = cpvm)
            }
        }

        /** Topbar **/

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){

            Button(onClick = {
                cpvm.setCardPopup(PopupType.WARNING, "Vuoi abbandonare la pagina?\n\nLe modifiche andranno perse!", WarningType.ANNULLA)
                cpvm.setDestination(Screen.Profile.route)
            }, modifier = Modifier
                .size(120.dp, 40.dp)
                .border(2.dp, RedCol, RoundedCornerShape(50)), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = RedCol
            )
            ) {
                Text(text = "Annulla", fontSize = 18.sp, style = MyTypography.typography.body2, color = RedCol)
            }

            Button(onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    cpvm.setDestination(Screen.Profile.route)
                    val isWarning = cf.saveProfile(cpvm, uDB)

                    if (isWarning == null)
                        cpvm.setCardPopup(PopupType.WARNING, "Sei sicuro di voler modificare le informazioni?", WarningType.MODIFICAPROFILO)

                    else
                        cpvm.setCardPopup(PopupType.ERROR, isWarning)
                }
            }, modifier = Modifier.size(120.dp, 40.dp), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(
                backgroundColor = BlueGradientCol,
                contentColor = Color.White
            )
            ) {
                Text(text = "Salva", fontSize = 17.sp, style = MyTypography.typography.body2)
            }
        }
    }
}