package com.example.classplayprototipofinale.ui.theme.form

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Delete
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
import com.example.classplayprototipofinale.screens.LinkType
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.TagCol
import com.example.classplayprototipofinale.ui.theme.home.TagsList
import com.google.firebase.storage.StorageReference

class CosplayFormPages {

    @SuppressLint("MutableCollectionMutableState")
    @OptIn(ExperimentalLayoutApi::class, ExperimentalCoilApi::class)
    @Composable
    fun Pagina1(cpvm: ClassPlayViewModel, ma: MainActivity, cosplaysImgsSRef: StorageReference) {

        var cosplayImgs by remember { mutableStateOf(cpvm.formImages.value!!) }

        cpvm.formImages.observe(ma) { cosplayImgs = it }

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
                    Text(text = "Inserisci le migliori foto\ndel tuo Cosplay", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        FlowRow (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp), horizontalArrangement = Arrangement.SpaceBetween){

            if (cosplayImgs.size < 6) {
                Column(modifier = Modifier
                    .padding(bottom = 20.dp)
                    .size(92.dp, 130.dp)
                    .background(Color.LightGray, RoundedCornerShape(20))
                    .clickable {
                        ma.uploadImage("cosplayImage")
                    }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Image(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Cosplay image", modifier = Modifier
                        .size(50.dp))
                }
            }

            for (img in cosplayImgs) {
                val painter = rememberImagePainter(data = img.value)

                Box(modifier = Modifier.size(92.dp, 130.dp)) {
                    Image(painter = painter, contentDescription = "Cosplay image", modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12)), contentScale = ContentScale.Crop)
                    Column(modifier = Modifier
                        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Column (modifier = Modifier
                            .size(40.dp)
                            .background(RedCol, RoundedCornerShape(50))
                            .clickable {
                                if (cpvm.cosplayEdit.value != null) {
                                    if (cpvm.cosplayEdit.value!!.imgUrls?.values?.contains(img.value) != true) {
                                        cosplaysImgsSRef.child(img.key).delete()
                                    }
                                }
                                cpvm.removeImgForm(img.key)
                            }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                            Icon(painter = painterResource(id = R.drawable.bin), contentDescription = "Rimuovi immagine", tint = Color.White, modifier = Modifier.size(30.dp))
                        }
                    }
                }
            }
            for (i in cosplayImgs.size..5) {
                Spacer(modifier = Modifier.size(90.dp, 120.dp))
            }
        }
    }

    @Composable
    fun Pagina2(cpvm: ClassPlayViewModel, ma: MainActivity) {

        val ctf = CosplayTextField()

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
                    Text(text = "Dai un titolo e una descrizione\nal tuo cosplay", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
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
            Text(text = "Titolo", fontSize = 22.sp, color = Color.White)
        }

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)){
            ctf.TitleTextField(cpvm = cpvm)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)){
            Text(text = "Descrizione", fontSize = 22.sp, color = Color.White)
        }

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)){
            ctf.DescriptionTextField(cpvm = cpvm)
        }
    }

    @Composable
    fun Pagina3(cpvm: ClassPlayViewModel, ma: MainActivity) {

        val ctf = CosplayTextField()
        val tl = TagsList()

        Box(modifier = Modifier.size(320.dp, 210.dp)) {
            Image(painter = painterResource(id = R.drawable.form_image_3), contentDescription = "Inserisci dei Tag per il tuo Cosplay", modifier = Modifier
                .fillMaxSize()
                .shadow(20.dp)
                .clip(RoundedCornerShape(6)),
                contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.75f), blendMode = BlendMode.Darken))

            Column (modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 3.dp)
                .padding(top = 12.dp), verticalArrangement = Arrangement.SpaceBetween){
                Text(text = "Tags", color = Color.White, fontSize = 35.sp)

                Row (modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Text(text = "Inserisci dei Tag per\nil tuo Cosplay", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)){
            ctf.TagTextField(cpvm = cpvm)
        }

        Spacer(modifier = Modifier.height(15.dp))

        tl.TagSliderForm(cpvm = cpvm, ma = ma)
    }

    @Composable
    fun Pagina4() {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.form_image_4), contentDescription = "In questa sezione potrai inserire Tempo, Materiali e Tutorial al tuo Cosplay. Non sei obbligato a completare tutti i passaggi e potrai aggiungere il Cosplay quando vuoi!", modifier = Modifier
                .fillMaxSize()
                .shadow(20.dp)
                .clip(RoundedCornerShape(6)),
                contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.6f), blendMode = BlendMode.Darken))

            Column (modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 15.dp), verticalArrangement = Arrangement.SpaceBetween){
                Text(text = "Inserimento \n Avanzato", color = Color.White, fontSize = 35.sp)

                Row (modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Text(text = "In questa sezione potrai inserire Tempo,\nMateriali e Tutorial al tuo Cosplay.\n\nNon sei obbligato a completare tutti i passaggi e potrai aggiungere il Cosplay\nquando vuoi!", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
                }
            }
        }
    }

    @Composable
    fun Pagina5() {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.form_image_5), contentDescription = "Inserendo queste informazioni renderai più visibile il tuo post e potrai condividere i tuoi metodi confrontandoti con la community di ClassPlay e aiutandola a migliorarsi!", modifier = Modifier
                .fillMaxSize()
                .shadow(20.dp)
                .clip(RoundedCornerShape(6)),
                contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.75f), blendMode = BlendMode.Darken))

            Column (modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 15.dp), verticalArrangement = Arrangement.SpaceBetween){
                Text(text = "Inserimento \n Avanzato", color = Color.White, fontSize = 35.sp)

                Row (modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Text(text = "Inserendo queste informazioni renderai\npiù visibile il tuo post e potrai condividere\ni tuoi metodi confrontandoti con la\ncommunity di ClassPlay e aiutandola a\nmigliorarsi!", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
                }
            }
        }
    }

    @Composable
    fun Pagina6(cpvm: ClassPlayViewModel) {

        val ft = FormTutorial()

        Box(modifier = Modifier.size(320.dp, 210.dp)) {
            Image(painter = painterResource(id = R.drawable.form_image_6), contentDescription = "Dicci quanto tempo hai impiegato per realizzare il Cosplay", modifier = Modifier
                .fillMaxSize()
                .shadow(20.dp)
                .clip(RoundedCornerShape(6)),
                contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.75f), blendMode = BlendMode.Darken))

            Column (modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 3.dp)
                .padding(top = 12.dp), verticalArrangement = Arrangement.SpaceBetween){
                Text(text = "Tempo", color = Color.White, fontSize = 35.sp)

                Row (modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Text(text = "Dicci quanto tempo hai\nimpiegato per realizzare il Cosplay", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ft.TimeForm(cpvm)
    }

    @Composable
    fun Pagina7(cpvm: ClassPlayViewModel) {

        val ft = FormTutorial()

        Box(modifier = Modifier.size(320.dp, 210.dp)) {
            Image(painter = painterResource(id = R.drawable.form_image_7), contentDescription = "Inserisci i materiali che hai usato per realizzare il cosplay", modifier = Modifier
                .fillMaxSize()
                .shadow(20.dp)
                .clip(RoundedCornerShape(6)),
                contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.75f), blendMode = BlendMode.Darken))

            Column (modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 3.dp)
                .padding(top = 12.dp), verticalArrangement = Arrangement.SpaceBetween){
                Text(text = "Materiali", color = Color.White, fontSize = 35.sp)

                Row (modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Text(text = "Inserisci i materiali che hai usato\nper realizzare il cosplay", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)){
            ft.MaterialForm(cpvm)
        }
    }

    @Composable
    fun OtherPages(cpvm: ClassPlayViewModel, ma: MainActivity, i: Int) {

        val ctf = CosplayTextField()

        var description by remember { mutableStateOf("") }
        var componentName by remember { mutableStateOf("") }
        var icon by remember { mutableIntStateOf(R.drawable.plus_image) }
        var link by remember { mutableStateOf("") }
        var linkType by remember { mutableStateOf<String>(LinkType.NONE.txt) }

        cpvm.cosplayFormTutorial.observe(ma) {
            if (it.size > i-8){
                description = it["s"+(i-8).toString()]?.description.toString()
                componentName = it["s"+(i-8).toString()]?.componentName.toString()
                icon = it["s"+(i-8).toString()]?.icon!!
                link = it["s"+(i-8).toString()]?.link.toString()
                linkType = it["s"+(i-8).toString()]?.linkType!!
            }
        }

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
                    Text(text = "Inserisci i tutorial che hai seguito o che\nproponi per realizzare il cosplay", fontSize = 15.sp, textAlign = TextAlign.Center, minLines = 2, color = Color.White)
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
                    Icon(painter = painterResource(id = icon), contentDescription = "Aggiungi icona", tint = TagCol, modifier = Modifier.clickable {
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

            ctf.StepTitleTextField(cpvm = cpvm, i = i-8, ma)

            Spacer(modifier = Modifier.height(10.dp))

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)){
                Text(text = "Tutorial", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(7.dp))

            Row {
                ctf.StepCosplaySearchTextField(cpvm = cpvm, i = i-8, ma = ma)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)){
                Text(text = "Descrizione", modifier = Modifier.width(180.dp), fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(7.dp))

            ctf.StepDescriptionTextField(cpvm = cpvm, i = i-8, ma)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}