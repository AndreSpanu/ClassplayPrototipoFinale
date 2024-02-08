package com.example.classplayprototipofinale.ui.theme.form

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
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.screens.AppIcons
import com.example.classplayprototipofinale.screens.PlusIcon
import com.example.classplayprototipofinale.ui.theme.BackgroundColBlur
import com.example.classplayprototipofinale.ui.theme.BlueGradientCol
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.DetailsCol
import com.example.classplayprototipofinale.ui.theme.GridCol
import com.example.classplayprototipofinale.ui.theme.StarCol


class CosplayGrid {
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun IconGrid(cpvm: ClassPlayViewModel, i: Int) {

        val tutorial = cpvm.cosplayFormTutorial.value!!

        Box(modifier = Modifier.fillMaxSize()) {
            Column (modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColBlur)
                .blur(20.dp)
                .clickable {
                    cpvm.setShowIconGrid(false)
                }){}

            Column (Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                FlowRow (
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .background(GridCol, RoundedCornerShape(16))
                        .padding(15.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (icon in AppIcons.values()) {
                        val painter = rememberImagePainter(data = icon.url)
                        Column (
                            Modifier
                                .padding(5.dp)
                                .size(65.dp)
                                .background(BottomBarCol, RoundedCornerShape(50)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                            Image(painter = painter, contentDescription = "Tutorial step icon", modifier = Modifier
                                .size(55.dp)
                                .clickable {
                                    cpvm.setShowIconGrid(false)
                                    if (icon.url == AppIcons.NONE.url) {
                                        tutorial["s$i"]?.icon = PlusIcon.PLUS.url
                                    } else {
                                        tutorial["s$i"]?.icon = icon.url
                                    }
                                    cpvm.updateTutorial(tutorial)
                                })
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun CosplayTutorialGrid(cpvm: ClassPlayViewModel, i: Int, ma: MainActivity) {

        var cosplaysList by remember { mutableStateOf(listOf<Cosplay>()) }
        var cosplaysListFiltered by remember { mutableStateOf(listOf<Cosplay>()) }

        val tutorial = cpvm.cosplayFormTutorial.value!!

        var search by remember { mutableStateOf("") }

        cpvm.cosplayList.observe(ma) { it ->
            cosplaysList = it
        }

        cpvm.currentCosplaySearch.observe(ma) { it -> search = it
            cosplaysListFiltered = cosplaysList.filter { it.cosplayName!!.contains(search, ignoreCase = true)} }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { }
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
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
                    .padding(15.dp)
            ) {
                Column (modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(6))){
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp), verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Chiudi scheda di ricerca", modifier = Modifier.clickable {
                            cpvm.setShowCosplayTutorialSearch(false)
                        })
                        TextField(value = search, onValueChange = {
                            search = it
                            cpvm.updateCosplaySearch(it)
                        }, textStyle = TextStyle(fontSize = 15.sp), colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ), modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawLine(
                                    color = Color.Gray,
                                    start = Offset(10.dp.toPx(), size.height - 15.dp.toPx()),
                                    end = Offset(
                                        size.width - 50.dp.toPx(),
                                        size.height - 15.dp.toPx()
                                    ),
                                    strokeWidth = 2.dp.toPx()
                                )
                            }, trailingIcon = {
                            Icon(painter = painterResource(id = R.drawable.lente_viola), contentDescription = null, modifier = Modifier.size(23.dp), tint = BottomBarCol)
                        })
                    }

                    Column (modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())){
                        for (cosplay in cosplaysListFiltered.take(10)) {
                            if (cosplay.tutorial != null) {
                                for (step in cosplay.tutorial!!) {
                                    val painter = rememberImagePainter(data = cosplay.imgUrls?.values?.first())
                                    val iconPainter = rememberImagePainter(data = step.value.icon)
                                    Row (modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            tutorial["s$i"]!!.link =
                                                cosplay.cosplayName + "::" + step.value.componentName

                                            tutorial["s$i"]!!.realAppLink =
                                                cosplay.tutorial!![step.key]?.realAppLink

                                            cpvm.updateTutorial(tutorial)

                                            cpvm.setShowCosplayTutorialSearch(false)
                                        }
                                        .padding(horizontal = 25.dp), verticalAlignment = Alignment.CenterVertically){
                                        Box(modifier = Modifier.size(60.dp, 80.dp)) {
                                            Image(painter = painter, contentDescription = cosplay.cosplayName, modifier = Modifier
                                                .fillMaxSize()
                                                .clip(
                                                    RoundedCornerShape(4)
                                                ), contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.6f), blendMode = BlendMode.Darken))
                                            Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                                                println(cosplay.cosplayName.toString())
                                                step.value.icon
                                                    ?.let { iconPainter }
                                                    ?.let { Icon(painter = it, contentDescription = step.value.componentName, tint = StarCol) }
                                            }

                                        }

                                        Spacer(modifier = Modifier.width(20.dp))

                                        Column (modifier = Modifier.height(80.dp), verticalArrangement = Arrangement.SpaceBetween){
                                            Text(text = cosplay.cosplayName!!)
                                            Text(text = step.value.componentName!!)
                                            Text(text = cosplay.username!!)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun TodoIconGrid(cpvm: ClassPlayViewModel, i: Int) {

        val tutorial = cpvm.todoFormTutorial.value!!

        Box(modifier = Modifier.fillMaxSize()) {
            Column (modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColBlur)
                .blur(20.dp)
                .clickable {
                    cpvm.setShowIconGrid(false)
                }){}

            Column (Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                FlowRow (
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .background(GridCol, RoundedCornerShape(16))
                        .padding(15.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (icon in AppIcons.values()) {

                        val painter = rememberImagePainter(data = icon.url)

                        Column (
                            Modifier
                                .padding(5.dp)
                                .size(65.dp)
                                .background(BottomBarCol, RoundedCornerShape(50)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                            Image(painter = painter, contentDescription = "Tutorial step icon", modifier = Modifier
                                .size(55.dp)
                                .clickable {
                                    cpvm.setShowIconGrid(false)
                                    if (icon.url == AppIcons.NONE.url) {
                                        tutorial["s$i"]?.icon = PlusIcon.PLUS.url
                                    } else {
                                        tutorial["s$i"]?.icon = icon.url
                                    }
                                    cpvm.updateTodoTutorial(tutorial)
                                })
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun TodoTutorialGrid(cpvm: ClassPlayViewModel, i: Int, ma: MainActivity) {

        var cosplaysList by remember { mutableStateOf(listOf<Cosplay>()) }
        var cosplaysListFiltered by remember { mutableStateOf(listOf<Cosplay>()) }

        val tutorial = cpvm.todoFormTutorial.value!!

        var search by remember { mutableStateOf("") }

        cpvm.cosplayList.observe(ma) { it ->
            cosplaysList = it
        }

        cpvm.currentCosplaySearch.observe(ma) { it -> search = it
            cosplaysListFiltered = cosplaysList.filter { it.cosplayName!!.contains(search, ignoreCase = true)} }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { }
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
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
                    .padding(15.dp)
            ) {
                Column (modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(6))){
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp), verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Chiudi scheda di ricerca", modifier = Modifier.clickable {
                            cpvm.setShowCosplayTutorialSearch(false)
                        })
                        TextField(value = search, onValueChange = {
                            search = it
                            cpvm.updateCosplaySearch(it)
                        }, textStyle = TextStyle(fontSize = 15.sp), colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ), modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawLine(
                                    color = Color.Gray,
                                    start = Offset(10.dp.toPx(), size.height - 15.dp.toPx()),
                                    end = Offset(
                                        size.width - 50.dp.toPx(),
                                        size.height - 15.dp.toPx()
                                    ),
                                    strokeWidth = 2.dp.toPx()
                                )
                            }, trailingIcon = {
                            Icon(painter = painterResource(id = R.drawable.lente_viola), contentDescription = null, modifier = Modifier.size(23.dp), tint = BottomBarCol)
                        })
                    }

                    Column (modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())){
                        for (cosplay in cosplaysListFiltered.take(10)) {
                            if (cosplay.tutorial != null) {
                                for (step in cosplay.tutorial!!) {
                                    val painter = rememberImagePainter(data = cosplay.imgUrls?.values?.first())

                                    val iconPainter = rememberImagePainter(data = step.value.icon)
                                    Row (modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            tutorial["s$i"]!!.link =
                                                cosplay.cosplayName + "::" + step.value.componentName

                                            tutorial["s$i"]!!.realAppLink =
                                                cosplay.tutorial!![step.key]?.realAppLink

                                            cpvm.updateTodoTutorial(tutorial)

                                            cpvm.setShowCosplayTutorialSearch(false)
                                        }
                                        .padding(horizontal = 25.dp), verticalAlignment = Alignment.CenterVertically){
                                        Box(modifier = Modifier.size(60.dp, 80.dp)) {
                                            Image(painter = painter, contentDescription = cosplay.cosplayName, modifier = Modifier
                                                .fillMaxSize()
                                                .clip(
                                                    RoundedCornerShape(4)
                                                ), contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.6f), blendMode = BlendMode.Darken))
                                            Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                                                Icon(painter = iconPainter, contentDescription = step.value.componentName, tint = StarCol)
                                            }

                                        }

                                        Spacer(modifier = Modifier.width(20.dp))

                                        Column (modifier = Modifier.height(80.dp), verticalArrangement = Arrangement.SpaceBetween){
                                            Text(text = cosplay.cosplayName!!)
                                            Text(text = step.value.componentName!!)
                                            Text(text = cosplay.username!!)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}