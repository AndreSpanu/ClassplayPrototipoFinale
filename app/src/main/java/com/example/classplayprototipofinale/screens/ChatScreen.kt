package com.example.classplayprototipofinale.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.ui.theme.MyTypography

@Composable
fun ChatScreen(cpvm: ClassPlayViewModel) {

    val context = LocalContext.current
    LaunchedEffect(true) {
        cpvm.notImplemented(context)
    }

    Column (modifier = Modifier.fillMaxSize()){
        
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .height(2.dp)
            .background(Color.White))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(id = R.drawable.immagine_chat), contentDescription = null, modifier = Modifier
                .size(60.dp)
                .clip(CircleShape), contentScale = ContentScale.Crop)
            
            Spacer(modifier = Modifier.width(10.dp))

            Column (modifier = Modifier.fillMaxWidth()){
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top){
                    Text(text = "DragonMaker", fontSize = 20.sp, style = MyTypography.typography.body2)
                    Text(text = "18:33", fontSize = 14.sp, style = MyTypography.typography.body1)
                }
                
                Text(text = "Ciao per realizzare la spada di Trun...", color = Color.LightGray, fontSize = 15.sp, style = MyTypography.typography.body1)
            }
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .height(2.dp)
            .background(Color.White))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(id = R.drawable.immagine_chat), contentDescription = null, modifier = Modifier
                .size(60.dp)
                .clip(CircleShape), contentScale = ContentScale.Crop)

            Spacer(modifier = Modifier.width(10.dp))

            Column (modifier = Modifier.fillMaxWidth()){
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top){
                    Text(text = "DragonMaker", fontSize = 20.sp, style = MyTypography.typography.body2)
                    Text(text = "18:33", fontSize = 14.sp, style = MyTypography.typography.body1)
                }

                Text(text = "Ciao per realizzare la spada di Trun...", color = Color.LightGray, fontSize = 15.sp, style = MyTypography.typography.body1)
            }
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .height(2.dp)
            .background(Color.White))

    }
    //Image(painter = painterResource(id = R.drawable.chat), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
}
