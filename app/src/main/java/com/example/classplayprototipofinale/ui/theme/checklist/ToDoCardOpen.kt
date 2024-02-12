package com.example.classplayprototipofinale.ui.theme.checklist

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.PopupType
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.WarningType
import com.example.classplayprototipofinale.models.ToDo
import com.example.classplayprototipofinale.models.ToDoStep
import com.example.classplayprototipofinale.navigation.Screen
import com.example.classplayprototipofinale.ui.theme.BlueGradientCol
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.MyTypography
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.ToDoCol
import com.google.firebase.database.DatabaseReference
import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mail
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.content.ContextCompat.startActivity
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.screens.LinkType
import com.example.classplayprototipofinale.ui.theme.BackgroundCol
import com.example.classplayprototipofinale.ui.theme.BackgroundColBlur
import com.example.classplayprototipofinale.ui.theme.BubbleCol
import com.example.classplayprototipofinale.ui.theme.DetailsCol
import com.example.classplayprototipofinale.ui.theme.GridCol
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.math.roundToInt

class ToDoCardOpen {
    @Composable
    fun TodoCardInfo(cpvm: ClassPlayViewModel, ma: MainActivity, uDB: DatabaseReference) {

        var todo by remember { mutableStateOf(ToDo()) }

        cpvm.todoCard.observe(ma) { if (it != null) todo = it }
        
        val painter = rememberImagePainter(data = todo.img?.values?.first())

        Column (
            Modifier
                .fillMaxSize()
                .background(BackgroundCol), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            Box(modifier = Modifier.size(350.dp, 580.dp)) {
                Column (modifier = Modifier
                    .fillMaxSize()
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
                    .padding(15.dp)){

                    Row (Modifier.padding(start = 5.dp)){
                        Image(painter = painter, contentDescription = "Profile icon", modifier = Modifier
                            .size(120.dp, 150.dp)
                            .clip(
                                RoundedCornerShape(10.dp)
                            ), contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(15.dp))

                        Column (modifier = Modifier.padding(top = 15.dp)){
                            todo.todoTitle?.let { Text(text = it, fontSize = 25.sp, style = MyTypography.typography.body2) }
                            Spacer(modifier = Modifier.height(20.dp))
                            todo.description?.let {
                                Text(text = it, fontSize = 15.sp, style = MyTypography.typography.body1)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "Componenti", fontSize = 25.sp, style = MyTypography.typography.body1)

                    Spacer(modifier = Modifier.height(15.dp))

                    if (todo.steps?.isNotEmpty() == true) {
                        for (step in todo.steps?.values!!) {

                            val painter = rememberImagePainter(data = step.icon)

                            val context = LocalContext.current

                            Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){

                                Text(text = step.step.toString(), style = MyTypography.typography.body1, fontSize = 18.sp)

                                Spacer(modifier = Modifier.width(10.dp))

                                Row (modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(15.dp))
                                    .padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween){

                                    Column(modifier = Modifier
                                        .size(25.dp)
                                        .shadow(5.dp, CircleShape)
                                        .background(ToDoCol, CircleShape), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                        Icon(painter = painter, contentDescription = "Icona dello step", tint = Color.White, modifier = Modifier.size(20.dp))
                                    }

                                    Column {
                                        Text(text = step.title!!, fontSize = 20.sp, style = MyTypography.typography.body1, color = Color.Black, modifier = Modifier
                                            .width(200.dp)
                                            .clickable {
                                                when (step.linkType) {

                                                    LinkType.LINK.txt -> {
                                                        val intent = Intent(
                                                            Intent.ACTION_VIEW,
                                                            Uri.parse(step.realAppLink)
                                                        )
                                                        context.startActivity(intent)
                                                    }

                                                    LinkType.APP.txt -> {
                                                        val intent = Intent(
                                                            Intent.ACTION_VIEW,
                                                            Uri.parse(step.realAppLink)
                                                        )
                                                        context.startActivity(intent)
                                                    }

                                                    LinkType.PHONE.txt -> {
                                                        cpvm.setTodoStepVideo(step)
                                                    }

                                                    else -> {}
                                                }
                                            }, textDecoration =
                                        if (step.linkType == LinkType.LINK.txt || step.linkType == LinkType.APP.txt)
                                            TextDecoration.Underline
                                        else
                                            TextDecoration.None
                                        )

                                        if (step.description?.isNotEmpty() == true) {
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(text = step.description!!, fontSize = 17.sp, style = MyTypography.typography.body1, color = Color.DarkGray, softWrap = true, modifier = Modifier
                                                .width(200.dp))
                                        }
                                    }

                                    var check by remember { mutableStateOf(cpvm.getCheck(todo.todoTitle!!, "s${step.step?.minus(1)}")) }

                                    Box(modifier = Modifier
                                        .size(25.dp)
                                        .border(3.dp, GridCol, CircleShape)
                                        .clickable {
                                            checkStep(uDB, step, todo, cpvm, check)
                                            check = !check
                                        }) {
                                        if (check)
                                            Image(painter = painterResource(id = R.drawable.check), contentDescription = "Fatto", modifier = Modifier.fillMaxSize(), colorFilter = ColorFilter.tint(GridCol))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

                Row (modifier = Modifier.fillMaxWidth().padding(7.dp), horizontalArrangement = Arrangement.End){
                    Icon(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Chiudi", modifier = Modifier.size(40.dp).rotate(45f).clickable {
                        cpvm.setTodoCard(null)
                    }, tint = Color.White)
                }

            }
        }

    }

    private fun checkStep(uDB: DatabaseReference, step: ToDoStep, todo: ToDo, cpvm: ClassPlayViewModel, check: Boolean) {
        val userUpdate = cpvm.currentUser.value

        userUpdate!!.yourToDo!![todo.todoTitle]!!.steps!!["s" + (step.step!! - 1).toString()]!!.completed =
            !check

        uDB.child(cpvm.username.value!!).setValue(userUpdate)
    }
}