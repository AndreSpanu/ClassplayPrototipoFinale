package com.example.classplayprototipofinale.ui.theme.home

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.PopupType
import com.example.classplayprototipofinale.ui.theme.BackgroundColBlur
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.StarCol
import kotlin.math.roundToInt
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.ui.theme.GridCol
import com.example.classplayprototipofinale.ui.theme.MyTypography
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.form.FormTutorial
import kotlinx.coroutines.delay


class PopupMenu {
    @Composable
    fun CardPopup(cpvm: ClassPlayViewModel) {

        var offset by remember { mutableFloatStateOf(200f) }

        val animatorStart = ValueAnimator.ofInt(200, 0).apply {
            duration = 500
            addUpdateListener { animation ->
                offset = animation.animatedValue.toString().toFloat()
            }
        }

        val animatorCloseTap = ValueAnimator.ofInt(0, 200).apply {
            duration = 500
            addUpdateListener { animation ->
                offset = animation.animatedValue.toString().toFloat()

                if (offset >= 190)
                    cpvm.setCardPopup(PopupType.NONE)
            }
        }

        val animatorCloseDrag = ValueAnimator.ofInt(offset.roundToInt(), 200).apply {
            duration = 500
            addUpdateListener { animation ->
                offset = animation.animatedValue.toString().toFloat()

                if (offset >= 190)
                    cpvm.setCardPopup(PopupType.NONE)
            }
        }

        LaunchedEffect(true) {
            animatorStart.start()
        }


        Box(modifier = Modifier.fillMaxSize()) {
            Column (
                Modifier
                    .fillMaxSize()
                    .background(BackgroundColBlur)
                    .blur(200.dp)
                    .clickable {
                        animatorCloseTap.start()
                    }){}

            Column (modifier = Modifier
                .fillMaxSize(), verticalArrangement = Arrangement.Bottom){
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = offset.dp)
                    .height(200.dp)
                    .background(
                        BottomBarCol,
                        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
                    .padding(bottom = 15.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween){
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    animatorCloseTap.start()
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(onDrag = { change, dragAmount ->
                                change.consume()
                                if (offset + dragAmount.y > 0)
                                    offset += dragAmount.y / 2
                            },
                                onDragEnd = {
                                    if (offset > 120f)
                                        animatorCloseDrag.start()
                                    else
                                        offset = 0f
                                })
                        }
                        .padding(top = 12.dp), horizontalArrangement = Arrangement.Center){
                        Box(modifier = Modifier
                            .width(60.dp)
                            .height(6.dp)
                            .background(Color.LightGray, RoundedCornerShape(50)))
                    }
                    Column (modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)){
                        Text(text = "Segnala", style = MyTypography.typography.body1, fontSize = 20.sp)
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))
                        Text(text = "Condividi", style = MyTypography.typography.body1, fontSize = 20.sp)
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))
                        Text(text = "Blocca Utente", style = MyTypography.typography.body1, fontSize = 20.sp)
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))
                    }
                }
            }
        }
    }



    @Composable
    fun ImpostazioniPopup(cpvm: ClassPlayViewModel, ma: MainActivity) {

        var offset by remember { mutableFloatStateOf(600f) }

        val animatorStart = ValueAnimator.ofInt(600, 0).apply {
            duration = 500
            addUpdateListener { animation ->
                offset = animation.animatedValue.toString().toFloat()
            }
        }

        val animatorCloseTap = ValueAnimator.ofInt(0, 600).apply {
            duration = 500
            addUpdateListener { animation ->
                offset = animation.animatedValue.toString().toFloat()

                if (offset >= 590) {
                    cpvm.setCardPopup(PopupType.NONE)
                }
            }
        }

        val animatorCloseDrag = ValueAnimator.ofInt(offset.roundToInt(), 600).apply {
            duration = 500
            addUpdateListener { animation ->
                offset = animation.animatedValue.toString().toFloat()

                if (offset >= 590) {
                    cpvm.setCardPopup(PopupType.NONE)
                }
            }
        }

        LaunchedEffect(true) {
            animatorStart.start()
        }


        Box(modifier = Modifier.fillMaxSize()) {
            Column (
                Modifier
                    .fillMaxSize()
                    .background(BackgroundColBlur)
                    .blur(200.dp)
                    .clickable {
                        animatorCloseTap.start()
                    }){}

            Column (modifier = Modifier
                .fillMaxSize(), verticalArrangement = Arrangement.Bottom){
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = offset.dp)
                    .height(600.dp)
                    .background(
                        BottomBarCol,
                        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
                    .clickable { }
                    .padding(bottom = 15.dp), horizontalAlignment = Alignment.CenterHorizontally){
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    animatorCloseTap.start()
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(onDrag = { change, dragAmount ->
                                change.consume()
                                if (offset + dragAmount.y > 0)
                                    offset += dragAmount.y / 2
                            },
                                onDragEnd = {
                                    if (offset > 200f)
                                        animatorCloseDrag.start()
                                    else
                                        offset = 0f
                                })
                        }
                        .padding(top = 12.dp), horizontalArrangement = Arrangement.Center){
                        Box(modifier = Modifier
                            .width(60.dp)
                            .height(6.dp)
                            .background(Color.LightGray, RoundedCornerShape(50)))
                    }
                    
                    Spacer(modifier = Modifier.height(30.dp))
                    
                    Column (modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)){

                        Text(text = "Notifiche", fontSize = 20.sp, style = MyTypography.typography.body1)

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))

                        Text(text = "Tempo di Utilizzo", fontSize = 20.sp, style = MyTypography.typography.body1)

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))

                        Text(text = "Lingua", fontSize = 20.sp, style = MyTypography.typography.body1)

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))

                        Text(text = "Aiuto", fontSize = 20.sp, style = MyTypography.typography.body1)

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))

                        Text(text = "Contatti", fontSize = 20.sp, style = MyTypography.typography.body1)

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))

                        Text(text = "Aggiungi Account", fontSize = 20.sp, style = MyTypography.typography.body1)

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))

                        Text(text = "Esci", fontSize = 20.sp, color = Color.White)

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))

                        Text(text = "Elimina Profilo", fontSize = 20.sp, color = RedCol, style = MyTypography.typography.body1)

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))
                    }
                }
            }
        }
    }

    @Composable
    fun FilterPopup(cpvm: ClassPlayViewModel, ma: MainActivity) {

        val ft = FormTutorial()

        var offset by remember { mutableFloatStateOf(600f) }
        var filter by remember { mutableStateOf(cpvm.filter.value!!) }

        cpvm.filter.observe(ma) { filter = it }

        val animatorStart = ValueAnimator.ofInt(600, 0).apply {
            duration = 500
            addUpdateListener { animation ->
                offset = animation.animatedValue.toString().toFloat()
            }
        }

        val animatorCloseTap = ValueAnimator.ofInt(0, 600).apply {
            duration = 500
            addUpdateListener { animation ->
                offset = animation.animatedValue.toString().toFloat()

                if (offset >= 590) {
                    cpvm.setCardPopup(PopupType.NONE)
                    cpvm.filterCosplayList()
                }
            }
        }

        val animatorCloseDrag = ValueAnimator.ofInt(offset.roundToInt(), 600).apply {
            duration = 500
            addUpdateListener { animation ->
                offset = animation.animatedValue.toString().toFloat()

                if (offset >= 590) {
                    cpvm.setCardPopup(PopupType.NONE)
                    cpvm.filterCosplayList()
                }
            }
        }

        LaunchedEffect(true) {
            animatorStart.start()
        }


        Box(modifier = Modifier.fillMaxSize()) {
            Column (
                Modifier
                    .fillMaxSize()
                    .background(BackgroundColBlur)
                    .blur(200.dp)
                    .clickable {
                        animatorCloseTap.start()
                    }){}

            Column (modifier = Modifier
                .fillMaxSize(), verticalArrangement = Arrangement.Bottom){
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = offset.dp)
                    .height(600.dp)
                    .background(
                        BottomBarCol,
                        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
                    .clickable { }
                    .padding(bottom = 15.dp), horizontalAlignment = Alignment.CenterHorizontally){
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    animatorCloseTap.start()
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(onDrag = { change, dragAmount ->
                                change.consume()
                                if (offset + dragAmount.y > 0)
                                    offset += dragAmount.y / 2
                            },
                                onDragEnd = {
                                    if (offset > 200f)
                                        animatorCloseDrag.start()
                                    else
                                        offset = 0f
                                })
                        }
                        .padding(top = 12.dp), horizontalArrangement = Arrangement.Center){
                        Box(modifier = Modifier
                            .width(60.dp)
                            .height(6.dp)
                            .background(Color.LightGray, RoundedCornerShape(50)))
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Column (modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)){

                        /** Filter Valutazione **/
                        Text(text = "Valutazione", fontSize = 20.sp, style = MyTypography.typography.body2)
                        Spacer(modifier = Modifier.height(10.dp))

                        MinMaxDropDown(cpvm = cpvm, ma)

                        Row (modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                            Row (verticalAlignment = Alignment.CenterVertically){
                                Text(text = "Cresc.", fontSize = 20.sp, style = MyTypography.typography.body2)
                                RadioButton(selected = filter.first, onClick = {
                                    if (!filter.first) {
                                        cpvm.setFilter(true)
                                    }
                                }, colors = RadioButtonDefaults.colors(
                                    selectedColor = Color.White,
                                    unselectedColor = Color.White
                                ))
                            }

                            Row (verticalAlignment = Alignment.CenterVertically){
                                Text(text = "Decresc.", fontSize = 20.sp, style = MyTypography.typography.body2)
                                RadioButton(selected = !filter.first, onClick = {
                                    if (filter.first) {
                                        cpvm.setFilter(false)
                                    }
                                }, colors = RadioButtonDefaults.colors(
                                    selectedColor = Color.White,
                                    unselectedColor = Color.White
                                ))
                            }
                        }

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))

                        Text(text = "Tempo di realizzazione max", fontSize = 20.sp, style = MyTypography.typography.body2)
                        Spacer(modifier = Modifier.height(10.dp))

                        ft.TimeForm(cpvm = cpvm)

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(Color.White))
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MinMaxDropDown(cpvm: ClassPlayViewModel, ma: MainActivity) {

        var filter by remember { mutableStateOf(cpvm.filter.value!!) }

        cpvm.filter.observe(ma) { filter = it }

        var minExpanded by remember { mutableStateOf(false) }
        var maxExpanded by remember { mutableStateOf(false) }

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            
            Text(text = "Minima", style = MyTypography.typography.body1, fontSize = 18.sp)

            ExposedDropdownMenuBox(expanded = minExpanded, onExpandedChange = { minExpanded = !minExpanded }, modifier = Modifier
                .width(200.dp)
                .height(40.dp)) {
                TextField(value = "", onValueChange = {}, readOnly = true, trailingIcon = {
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                        for (i in 1..5) {
                            if (i <= filter.second)
                                Icon(painter = painterResource(id = R.drawable.star_1), contentDescription = null, tint = StarCol, modifier = Modifier.size(25.dp))
                            else
                                Icon(painter = painterResource(id = R.drawable.star_0), contentDescription = null, tint = StarCol, modifier = Modifier.size(25.dp))
                        }
                        if (minExpanded)
                            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Chiudi menu")
                        else
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Apri menu")
                    } },
                    modifier = Modifier.menuAnchor(), shape = RoundedCornerShape(20), colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    )
                )

                ExposedDropdownMenu(expanded = minExpanded, onDismissRequest = { minExpanded = false }, modifier = Modifier.background(Color.White)) {
                    for (i in 0..5) {
                        DropdownMenuItem(text = { Row (modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                            for (j in 1..5) {
                                if (j <= i)
                                    Icon(painter = painterResource(id = R.drawable.star_1), contentDescription = null, tint = StarCol, modifier = Modifier.size(25.dp))
                                else
                                    Icon(painter = painterResource(id = R.drawable.star_0), contentDescription = null, tint = StarCol, modifier = Modifier.size(25.dp))
                            }
                        } }, onClick = {
                            if (i <= filter.third) {
                                cpvm.setFilter(min = i)
                                minExpanded = false
                            }
                        })

                        if (i != 5)
                            Spacer(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp).height(1.dp).background(Color.Gray))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(10.dp))

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){

            Text(text = "Massima", style = MyTypography.typography.body1, fontSize = 18.sp)

            ExposedDropdownMenuBox(expanded = maxExpanded, onExpandedChange = { maxExpanded = !maxExpanded }, modifier = Modifier
                .width(200.dp)
                .height(40.dp)) {
                TextField(value = "", onValueChange = {}, readOnly = true, trailingIcon = {
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                            .padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                        for (i in 1..5) {
                            if (i <= filter.third)
                                Icon(painter = painterResource(id = R.drawable.star_1), contentDescription = null, tint = StarCol, modifier = Modifier.size(25.dp))
                            else
                                Icon(painter = painterResource(id = R.drawable.star_0), contentDescription = null, tint = StarCol, modifier = Modifier.size(25.dp))
                        }
                        if (maxExpanded)
                            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Chiudi menu")
                        else
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Apri menu")
                    } },
                    modifier = Modifier.menuAnchor(), shape = RoundedCornerShape(20), colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    ), textStyle = MyTypography.typography.body1.copy(fontSize = 18.sp)
                )

                ExposedDropdownMenu(expanded = maxExpanded, onDismissRequest = { maxExpanded = false }, modifier = Modifier.background(Color.White)) {
                    for (i in 0..5) {
                        DropdownMenuItem(text = { Row (modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                            .padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                            for (j in 1..5) {
                                if (j <= i)
                                    Icon(painter = painterResource(id = R.drawable.star_1), contentDescription = null, tint = StarCol, modifier = Modifier.size(25.dp))
                                else
                                    Icon(painter = painterResource(id = R.drawable.star_0), contentDescription = null, tint = StarCol, modifier = Modifier.size(25.dp))
                            }
                        }
                        }, onClick = {
                            if (i >= filter.second) {
                                cpvm.setFilter(max = i)
                                maxExpanded = false
                            }
                        })

                        if (i != 5)
                            Spacer(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp).height(1.dp).background(Color.Gray))
                    }
                }
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun ErrorPopup(cpvm: ClassPlayViewModel) {
        var messageOffset by remember { mutableFloatStateOf(200f) }
        var end by remember { mutableStateOf(false) }

        LaunchedEffect(true) {
            ValueAnimator.ofFloat(140f, 0f).apply {
                duration = 500
                addUpdateListener { animation ->
                    messageOffset = animation.animatedValue.toString().toFloat()
                }
                start()
            }
            delay(3000)
            if (!end) {
                ValueAnimator.ofFloat(0f, 140f).apply {
                    duration = 500
                    addUpdateListener { animation ->
                        messageOffset = animation.animatedValue.toString().toFloat()
                    }
                    start()
                }
            }
            delay(500)
            cpvm.setCardPopup(PopupType.NONE)
        }

        val endAnimator = ValueAnimator.ofFloat(0f, 140f).apply {
            duration = 500
            addUpdateListener { animation ->
                messageOffset = animation.animatedValue.toString().toFloat()
            }
        }

        val message = cpvm.cardPopup.value?.second
        Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom){
            Column(modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .offset(y = messageOffset.dp)
                .background(
                    BottomBarCol,
                    RoundedCornerShape(topStartPercent = 10, topEndPercent = 10)
                )
                .padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                Text(text = message ?: "", textAlign = TextAlign.Center, fontSize = 23.sp, style = MyTypography.typography.body1)
                Button(onClick = { endAnimator.start(); end = true }, colors = ButtonDefaults.buttonColors(
                    backgroundColor = GridCol),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(text = "Ok", fontSize = 20.sp, style = MyTypography.typography.body1)
                }
            }
        }
    }
}