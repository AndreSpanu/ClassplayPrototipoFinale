package com.example.classplayprototipofinale.ui.theme.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.ui.theme.BackgroundCol
import com.example.classplayprototipofinale.ui.theme.MyTypography

class Video {

    @Composable
    fun ShowVideo(cpvm: ClassPlayViewModel) {

        val step by remember { mutableStateOf(cpvm.stepVideo.value) }

        Column (modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCol)
            .clickable {}, verticalArrangement = Arrangement.Bottom){
            Column (modifier = Modifier
                .fillMaxHeight(0.75f)
                .fillMaxWidth()){
                Image(painter = painterResource(id = R.drawable.video), contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        cpvm.setStepVideo(null)
                    }, contentScale = ContentScale.FillWidth)

                Column (modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)){
                    step!!.description?.let { Text(text = it, style = MyTypography.typography.body1, fontSize = 20.sp) }
                }
            }
        }
    }

}