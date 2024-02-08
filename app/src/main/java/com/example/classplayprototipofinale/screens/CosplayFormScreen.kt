package com.example.classplayprototipofinale.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.PopupType
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.WarningType
import com.example.classplayprototipofinale.navigation.Screen
import com.example.classplayprototipofinale.ui.theme.BlueGradientCol
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.StarCol
import com.example.classplayprototipofinale.ui.theme.TagCol
import com.example.classplayprototipofinale.ui.theme.form.CheckForm
import com.example.classplayprototipofinale.ui.theme.form.CosplayFormPages
import com.example.classplayprototipofinale.ui.theme.form.CosplayGrid
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun CosplayFormScreen(navController: NavController, cpvm: ClassPlayViewModel, ma: MainActivity, cpDB: DatabaseReference, cosplaysImgsSRef: StorageReference) {

    val cg = CosplayGrid()
    val cfp = CosplayFormPages()
    val cf = CheckForm()

    val density = LocalDensity.current.density

    val stepDistance = (320.dp * density).value
    var newStep by remember { mutableIntStateOf(0) }
    var currentStep by remember { mutableIntStateOf(cpvm.currentStep.value!!) }
    var totalSteps by remember { mutableIntStateOf(cpvm.totalSteps.value!!) }

    var showIconGrid by remember { mutableStateOf(false) }
    var changeStepGrid by remember { mutableStateOf(false) }
    var searchCosplayTutorial by remember { mutableStateOf(false) }

    var message by remember { mutableStateOf<String?>("") }

    cpvm.currentStep.observe(ma) { currentStep = it }
    cpvm.totalSteps.observe(ma) { totalSteps = it }
    cpvm.showIconGrid.observe(ma) { showIconGrid = it }
    cpvm.showCosplayTutorialSearch.observe(ma) { searchCosplayTutorial = it }

    val tutorialIcons = listOf(R.drawable.none, R.drawable.hat, R.drawable.wig, R.drawable.tshirt, R.drawable.gloves, R.drawable.shoes, R.drawable.trousers,  R.drawable.sword)

    val animatedScrollValue = remember { Animatable((currentStep - 1) * stepDistance) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun animateScroll() {
        if (!isAnimating && !searchCosplayTutorial) {
            isAnimating = true
            coroutineScope.launch {
                animatedScrollValue.animateTo((newStep-1) * stepDistance, animationSpec = tween(500))
                isAnimating = false
            }
        }
    }

    Box (Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier
                .size(320.dp, 530.dp)
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
            ) {
                var dragEnd = 0f
                Row (modifier = Modifier
                    .size(320.dp, 530.dp)
                    .wrapContentSize(Alignment.Center, false)
                    .horizontalScroll(ScrollState(animatedScrollValue.value.toInt()))
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { dragEnd = 0f },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (!isAnimating)
                                    dragEnd -= dragAmount.x
                            },
                            onDragEnd = {
                                if (dragEnd > 200 && !isAnimating && (currentStep < totalSteps) || (currentStep == totalSteps && totalSteps == 7)) {
                                    newStep = currentStep + 1
                                    animateScroll()
                                    cpvm.setCurrentStep(newStep)
                                } else if (dragEnd < -200 && !isAnimating && currentStep > 1) {
                                    newStep = currentStep - 1
                                    animateScroll()
                                    cpvm.setCurrentStep(newStep)
                                }
                            }
                        )
                    }){
                    for (i in 1..totalSteps) {

                        Column (modifier = Modifier
                            .size(320.dp, 530.dp), horizontalAlignment = Alignment.CenterHorizontally){

                            when (i) {
                                1 -> cfp.Pagina1(cpvm = cpvm, ma = ma, cosplaysImgsSRef = cosplaysImgsSRef)

                                2 -> cfp.Pagina2(cpvm = cpvm, ma = ma)

                                3 -> cfp.Pagina3(cpvm = cpvm, ma = ma)

                                4 -> cfp.Pagina4()

                                5 -> cfp.Pagina5()

                                6 -> cfp.Pagina6(cpvm = cpvm)

                                7 -> cfp.Pagina7(cpvm = cpvm)

                                else -> {
                                    if (cpvm.cosplayFormTutorial.value != null) {
                                        if (cpvm.cosplayFormTutorial.value!!.size >= i-7)
                                            cfp.OtherPages(cpvm = cpvm, ma = ma, i = i)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        /** Bottoni Annulla e Aggiungi **/

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            
            Button(onClick = {
                cpvm.setCardPopup(PopupType.WARNING, "Vuoi abbandonare la pagina?\n\nLe modifiche andranno perse!", WarningType.ANNULLA)
                cpvm.setDestination(Screen.Home.route)
            }, modifier = Modifier
                .size(120.dp, 40.dp)
                .border(2.dp, RedCol, RoundedCornerShape(50)), shape = RoundedCornerShape(50), colors = buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = RedCol
            )) {
                Text(text = "Annulla", fontSize = 18.sp)
            }

            Button(onClick = {
                val risultato = cf.cosplayForm(cpvm = cpvm, cpDB, navController, cosplaysImgsSRef)
                message = risultato?.first
                    if ( message != null) {
                        newStep = risultato!!.second
                        if (!isAnimating) {
                            animateScroll()
                            cpvm.setCurrentStep(newStep)
                        }
                        if (newStep < 7) {
                            cpvm.setCardPopup(PopupType.ERROR, message!!)
                        }
                        else {
                            cpvm.setDestination(Screen.Home.route)
                            cpvm.setCardPopup(PopupType.WARNING, message!!, WarningType.COSPLAYSTEPMANCANTI)
                        }
                    }
                }, modifier = Modifier.size(120.dp, 40.dp), shape = RoundedCornerShape(50), colors = buttonColors(
                backgroundColor = BlueGradientCol,
                contentColor = Color.White
            )) {
                Text(text = "Aggiungi", fontSize = 17.sp)
            }
        }

        /** Bottoni di spostamento tra le pagine **/

        Row (Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
            if (currentStep > 1) {
                Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Step precedente", modifier = Modifier
                    .size(30.dp, 50.dp)
                    .clickable {
                        if (!isAnimating) {
                            newStep = currentStep - 1
                            animateScroll()
                            cpvm.setCurrentStep(newStep)
                        }
                    }, tint = Color.White)
            }
            else {
                Spacer(modifier = Modifier)
            }
            if (currentStep != totalSteps || totalSteps <= 7) {
                Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Step precedente", modifier = Modifier
                    .size(30.dp, 50.dp)
                    .rotate(180F)
                    .clickable {
                        if (currentStep == totalSteps) {
                            cpvm.newTutorialStep()
                            cpvm.updateTotalSteps(1)
                        }
                        if (!isAnimating) {
                            newStep = currentStep + 1
                            animateScroll()
                            cpvm.setCurrentStep(newStep)
                        }
                    }, tint = Color.White)
            }
        }


        /** Bottoni di cambio ordine **/

        if (currentStep > 7 && !changeStepGrid) {
            Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom){
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    Column (modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            changeStepGrid = true
                        }
                        .background(BlueGradientCol, RoundedCornerShape(35)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                        Text(text = (currentStep - 7).toString(), fontSize = 30.sp, color = Color.White)
                    }

                    if (currentStep == totalSteps) {
                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Aggiungi tutorial", tint = TagCol, modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                cpvm.newTutorialStep()
                                cpvm.updateTotalSteps(1)
                                if (!isAnimating) {
                                    newStep = totalSteps
                                    cpvm.setCurrentStep(newStep)
                                    animateScroll()
                                }
                            })
                    }
                }
            }
        }
        else if (currentStep > 7) {
            Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom){
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 15.dp, vertical = 7.dp)
                    .background(StarCol, RoundedCornerShape(30))
                    .padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Row (modifier = Modifier
                        .fillMaxHeight()
                        .horizontalScroll(rememberScrollState()), verticalAlignment = Alignment.CenterVertically){
                        Column (modifier = Modifier
                            .size(45.dp)
                            .background(BlueGradientCol, RoundedCornerShape(35)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                            Text(text = (currentStep - 7).toString(), fontSize = 30.sp, color = Color.White)
                        }

                        for (i in 8..totalSteps) {
                            if (i != currentStep) {
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Column (modifier = Modifier
                                    .size(45.dp)
                                    .clickable {
                                        cpvm.changeStepPosition(currentStep - 8, i - 8)
                                        if (!isAnimating) {
                                            newStep = i
                                            animateScroll()
                                            cpvm.setCurrentStep(newStep)
                                        }
                                    }
                                    .background(BottomBarCol, RoundedCornerShape(35)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                                    Text(text = (i - 7).toString(), fontSize = 30.sp, color = Color.White)
                                }
                            }
                        }
                    }

                    Icon(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Chiudi", modifier = Modifier
                        .size(45.dp)
                        .rotate(45F)
                        .clickable {
                            changeStepGrid = false
                        }, tint = Color.White)
                }
            }
        }
    }

    /** Griglia scelta icona **/
    if (showIconGrid) {
        Column (Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            cg.IconGrid(iconList = tutorialIcons, cpvm = cpvm, i = currentStep-8)
        }
    }
    else if(searchCosplayTutorial){
        cg.CosplayTutorialGrid(cpvm = cpvm, i = currentStep - 8, ma)
    }
}

enum class LinkType(var txt: String) {
    APP("Cerca nell'App"),
    PHONE("Carica dal telefono"),
    LINK("Link"),
    NONE("Seleziona tipo di link")
}