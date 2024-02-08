package com.example.classplayprototipofinale.ui.theme.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.models.ToDoStep
import com.example.classplayprototipofinale.models.TutorialStep
import com.example.classplayprototipofinale.navigation.Screen
import com.example.classplayprototipofinale.ui.theme.BackgroundCol
import com.example.classplayprototipofinale.ui.theme.BackgroundColBlur
import com.example.classplayprototipofinale.ui.theme.BlueGradientCol
import com.example.classplayprototipofinale.ui.theme.BubbleCol
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.DetailsCol
import com.example.classplayprototipofinale.ui.theme.GridCol
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.TagCol
import com.example.classplayprototipofinale.ui.theme.WarningCol
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlin.math.roundToInt

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
                    step!!.description?.let { Text(text = it, color = Color.White, fontSize = 20.sp) }
                }
            }
        }
    }

}