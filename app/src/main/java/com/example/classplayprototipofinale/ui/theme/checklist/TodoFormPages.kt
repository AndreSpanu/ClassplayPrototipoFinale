package com.example.classplayprototipofinale.ui.theme.checklist

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.screens.AppIcons
import com.example.classplayprototipofinale.screens.LinkType
import com.example.classplayprototipofinale.screens.PlusIcon
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.StarCol
import com.example.classplayprototipofinale.ui.theme.TagCol
import com.google.firebase.storage.StorageReference

class TodoFormPages {

    @SuppressLint("MutableCollectionMutableState")
    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun Pagina1(cpvm: ClassPlayViewModel, ma: MainActivity, cosplaysImgsSRef: StorageReference) {

        var todoImgMap by remember { mutableStateOf(cpvm.formImages.value!!) }

        cpvm.formImages.observe(ma) { todoImgMap = it }

        Box(modifier = Modifier.size(320.dp, 210.dp)) {
            Image(painter = painterResource(id = R.drawable.form_image_1), contentDescription = "Inserisci le foto del tuo cosplay", modifier = Modifier
                .fillMaxSize()
                .shadow(20.dp)
                .clip(RoundedCornerShape(6)),
                contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.75f), blendMode = BlendMode.Darken))

            Column (modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 3.dp)
                .padding(top = 12.dp), verticalArrangement = Arrangement.SpaceBetween){
                Text(text = "Foto", color = Color.White, fontSize = 35.sp)

                Row (modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Text(text = "Inserisci una foto per il tuo ToDo", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (todoImgMap.isNotEmpty()) {
            val imgUrl = todoImgMap[todoImgMap.keys.first()]

            val painter = rememberImagePainter(data = imgUrl)

            Box(modifier = Modifier.size(190.dp, 260.dp)) {
                Image(painter = painter, contentDescription = "Todo image", modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4)), contentScale = ContentScale.Crop)
                Column(modifier = Modifier
                    .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Column (modifier = Modifier
                        .size(40.dp)
                        .background(RedCol, RoundedCornerShape(50))
                        .clickable {
                            if ((cpvm.todoEdit.value?.img?.keys?.first()
                                    ?: "") == todoImgMap.keys.first()
                            ) {
                                cpvm.removeImgForm(todoImgMap.keys.first())
                                return@clickable
                            }

                            cosplaysImgsSRef
                                .child(todoImgMap.keys.first())
                                .delete()
                            cpvm.removeImgForm(todoImgMap.keys.first())
                        }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                        Icon(painter = painterResource(id = R.drawable.bin), contentDescription = "Rimuovi immagine", tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
        else {
            Column(modifier = Modifier
                .padding(bottom = 20.dp)
                .size(190.dp, 260.dp)
                .background(Color.LightGray, RoundedCornerShape(6))
                .clickable {
                    ma.uploadImage("cosplayImage")
                }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Image(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Cosplay image", modifier = Modifier
                    .size(50.dp))
            }
        }
    }

    @Composable
    fun Pagina2(cpvm: ClassPlayViewModel, ma: MainActivity) {

        val ttf = TodoTextField()

        Box(modifier = Modifier.size(320.dp, 210.dp)) {
            Image(painter = painterResource(id = R.drawable.form_image_2), contentDescription = "Dai un titolo e una descrizione al tuo cosplay", modifier = Modifier
                .fillMaxSize()
                .shadow(20.dp)
                .clip(RoundedCornerShape(6)),
                contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.75f), blendMode = BlendMode.Darken))

            Column (modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 3.dp)
                .padding(top = 12.dp), verticalArrangement = Arrangement.SpaceBetween){
                Text(text = "Titolo", color = Color.White, fontSize = 35.sp)

                Row (modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Text(text = "Dai un titolo e una descrizione\nalla tua ToDo", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        var title by remember { mutableStateOf(cpvm.formTitle.value) }
        cpvm.formTitle.observe(ma) { title = it }

        var description by remember { mutableStateOf(cpvm.formDescription.value!!) }

        cpvm.formDescription.observe(ma) { description = it }

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)){
            Text(text = "Titolo ", fontSize = 22.sp, color = Color.White)
            Text(text = "*", fontSize = 22.sp, color = StarCol)
        }

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)){
            ttf.TitleTextField(cpvm = cpvm)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)){
            Text(text = "Descrizione ", fontSize = 22.sp, color = Color.White)
            Text(text = "*", fontSize = 22.sp, color = StarCol)
        }

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)){
            ttf.DescriptionTextField(cpvm = cpvm)
        }
    }

    @Composable
    fun OtherPages(cpvm: ClassPlayViewModel, ma: MainActivity, i: Int) {

        val ttf = TodoTextField()

        var description by remember { mutableStateOf("") }
        var componentName by remember { mutableStateOf("") }
        var icon by remember { mutableStateOf(PlusIcon.PLUS.url) }
        var link by remember { mutableStateOf("") }
        var linkType by remember { mutableStateOf<String>(LinkType.NONE.txt) }

        cpvm.todoFormTutorial.observe(ma) {
            if (it.size > i-3){
                description = it["s"+(i-3).toString()]?.description.toString()
                componentName = it["s"+(i-3).toString()]?.title.toString()
                icon = it["s"+(i-3).toString()]?.icon!!
                link = it["s"+(i-3).toString()]?.link.toString()
                linkType = it["s"+(i-3).toString()]?.linkType!!
            }
        }

        val painter = rememberImagePainter(data = icon)

        Box(modifier = Modifier.size(320.dp, 210.dp)) {
            Image(painter = painterResource(id = R.drawable.form_image_other), contentDescription = "Inserisci il tutorial che hai seguito o che proponi per realizzare il cosplay", modifier = Modifier
                .fillMaxSize()
                .shadow(20.dp, RoundedCornerShape(8))
                .clip(RoundedCornerShape(6)),
                contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.75f), blendMode = BlendMode.Darken))

            Column (modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 3.dp)
                .padding(top = 12.dp), verticalArrangement = Arrangement.SpaceBetween){
                Text(text = "Tutorial", color = Color.White, fontSize = 35.sp)

                Row (modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Text(text = "Inserisci i tutorial che ti servono per\ncompletare il tuo cosplay", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
                }
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(
                rememberScrollState()
            )) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically){
                Column(modifier = Modifier
                    .size(45.dp)
                    .background(Color.LightGray, RoundedCornerShape(50)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    Icon(painter = painter, contentDescription = "Aggiungi icona", tint = TagCol, modifier = Modifier.clickable {
                        cpvm.setShowIconGrid(true)
                    })
                }

                Spacer(modifier = Modifier.width(20.dp))

                Text(text = "Inserisci icona", fontSize = 18.sp, color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)){
                Text(text = "Componente", fontSize = 18.sp, color = Color.White)
            }

            ttf.StepTitleTextField(cpvm = cpvm, i = i-3, ma)

            Spacer(modifier = Modifier.height(10.dp))

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)){
                Text(text = "Tutorial", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(7.dp))

            Row {
                ttf.StepCosplaySearchTextField(cpvm = cpvm, i = i-3, ma = ma)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)){
                Text(text = "Descrizione", modifier = Modifier.width(180.dp), fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(7.dp))

            ttf.StepDescriptionTextField(cpvm = cpvm, i = i-3, ma)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}