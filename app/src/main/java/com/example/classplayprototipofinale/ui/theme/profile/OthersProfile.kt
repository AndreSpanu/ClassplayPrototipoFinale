package com.example.classplayprototipofinale.ui.theme.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.ui.theme.BackgroundColBlur
import com.example.classplayprototipofinale.ui.theme.BlueGradientCol
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.MyTypography
import com.google.firebase.database.DatabaseReference

class OthersProfile {

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun Profile(cpvm: ClassPlayViewModel, cpDB: DatabaseReference, ma: MainActivity) {

        val pc = ProfileCard()
        var profileZoom by remember { mutableStateOf(false) }
        var user by remember { mutableStateOf(Users()) }

        cpvm.otherProfile.observe(ma) {
            if (it != null) {
                user = it
            }
        }

        val painter = rememberImagePainter(data = user.profileImgUrl)

        val cosplayList by remember { mutableStateOf(cpvm.cosplayList.value!!) }



        Box(Modifier.fillMaxSize()) {
            Column (
                Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

                Box(modifier = Modifier
                    .size(350.dp, 580.dp)) {

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

                            Column (Modifier.padding(top = 10.dp)){
                                user.username?.let { Text(text = it, fontSize = 25.sp, style = MyTypography.typography.body1) }
                                Spacer(modifier = Modifier.height(20.dp))
                                user.bio?.let { Text(text = it, style = MyTypography.typography.body1, fontSize = 15.sp) }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row (modifier = Modifier.padding(horizontal = 25.dp), verticalAlignment = Alignment.CenterVertically){
                            Icon(imageVector = Icons.Default.Call, contentDescription = "Numero di telefono", tint = Color.White)
                            Spacer(modifier = Modifier.width(15.dp))
                            user.phoneNumber?.let { Text(text = it, style = MyTypography.typography.body1, fontSize = 20.sp) }
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        Row (modifier = Modifier.padding(horizontal = 25.dp), verticalAlignment = Alignment.CenterVertically){
                            Icon(imageVector = Icons.Default.Mail, contentDescription = "Email", tint = Color.White)
                            Spacer(modifier = Modifier.width(15.dp))
                            user.emailAddress?.let { Text(text = it, style = MyTypography.typography.body1, fontSize = 20.sp) }
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
                            for (cosplay in cosplayList.filter { it.username == user.username }) {
                                pc.ProfileCardShow(cpvm = cpvm, cosplay = cosplay, cpDB = cpDB)
                            }
                        }
                    }

                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier
                            .size(30.dp)
                            .background(Color.White, RoundedCornerShape(50))) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close the card", modifier = Modifier
                                .clickable {
                                    cpvm.setOtherProfile(null)
                                }
                                .fillMaxSize(), tint = BottomBarCol)
                        }
                    }
                }

            }

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
    }
}