package com.example.classplayprototipofinale.ui.theme.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.ui.theme.BubbleCol
import com.example.classplayprototipofinale.ui.theme.MyTypography
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ProfileCard {

    /** La card nella miniatura del profilo **/

    @Composable
    fun ProfileCardShow(cpvm: ClassPlayViewModel, cosplay: Cosplay, cpDB: DatabaseReference) {

        var avgReview by remember { mutableStateOf<String?>("0") }

        cosplay.cosplayName?.let {
            cpDB.child(it).addValueEventListener(object :  ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        avgReview = snapshot.child("avgReviews").value as String?
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        val painter = rememberImagePainter(data = cosplay.imgUrls?.values?.first())

        Box(modifier = Modifier
            .padding(bottom = 15.dp)
            .padding(horizontal = 15.dp)
            .size(130.dp, 160.dp)) {
            Image(painter = painter,
                contentDescription = cosplay.cosplayName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(13.dp))
                    .clickable {
                        cpvm.setZoomCard(cosplay)
                    }, contentScale = ContentScale.Crop
            )
            Column (Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween){
                Row (
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, end = 5.dp), horizontalArrangement = Arrangement.End){
                    Row (
                        Modifier
                            .background(BubbleCol, RoundedCornerShape(50))
                            .padding(horizontal = 5.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                        Text(text = avgReview.toString(), style = MyTypography.typography.body1, fontSize = 3.em)
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(painter = painterResource(id = R.drawable.star_1), contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                }
                Row (
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp), horizontalArrangement = Arrangement.Center){
                    Row (
                        Modifier
                            .background(BubbleCol, RoundedCornerShape(50))
                            .padding(horizontal = 5.dp, vertical = 3.dp)){
                        cosplay.cosplayName?.let { Text(text = it, style = MyTypography.typography.body1, fontSize = 3.em) }
                    }
                }
            }
        }
    }

}