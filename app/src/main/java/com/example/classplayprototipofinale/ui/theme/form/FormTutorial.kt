package com.example.classplayprototipofinale.ui.theme.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.ui.theme.StarCol

class FormTutorial {
    @Composable
    fun TimeForm(cpvm: ClassPlayViewModel) {

        var anni by remember { mutableIntStateOf(cpvm.cosplayFormTime.value?.get("anni")!!.toInt()) }
        var mesi by remember { mutableIntStateOf(cpvm.cosplayFormTime.value?.get("mesi")!!.toInt()) }
        var settimane by remember { mutableIntStateOf(cpvm.cosplayFormTime.value?.get("settimane")!!.toInt()) }
        var giorni by remember { mutableIntStateOf(cpvm.cosplayFormTime.value?.get("giorni")!!.toInt()) }
        var ore by remember { mutableIntStateOf(cpvm.cosplayFormTime.value?.get("ore")!!.toInt()) }

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Row (modifier = Modifier.width(100.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Icon(painter = painterResource(id = R.drawable.minus_image), contentDescription = "Togli", tint = Color.White, modifier = Modifier.clickable {
                    if (anni > 0) {
                        anni--
                        cpvm.updateTimeForm("anni", anni.toString())
                    }

                })
                Text(text = anni.toString(), color = Color.White, fontSize = 18.sp)
                Icon(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Aggiungi", tint = Color.White, modifier = Modifier.clickable {
                    anni++
                    cpvm.updateTimeForm("anni", anni.toString())
                })
            }

            Row (modifier = Modifier.width(90.dp).background(StarCol, RoundedCornerShape(50)).padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Text(text = "Anni", fontSize = 18.sp, color = Color.White)
            }
        }
        
        Spacer(modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .height(2.dp)
            .fillMaxWidth()
            .background(Color.White))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Row (modifier = Modifier.width(100.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Icon(painter = painterResource(id = R.drawable.minus_image), contentDescription = "Togli", tint = Color.White, modifier = Modifier.clickable {
                    if (mesi > 0){
                        mesi--
                        cpvm.updateTimeForm("mesi", mesi.toString())
                    }
                })
                Text(text = mesi.toString(), color = Color.White, fontSize = 18.sp)
                Icon(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Aggiungi", tint = Color.White, modifier = Modifier.clickable {
                    if (mesi < 11) {
                        mesi++
                        cpvm.updateTimeForm("mesi", mesi.toString())
                    }
                })
            }

            Row (modifier = Modifier.width(90.dp).background(StarCol, RoundedCornerShape(50)).padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Text(text = "Mesi", fontSize = 18.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .height(2.dp)
            .fillMaxWidth()
            .background(Color.White))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Row (modifier = Modifier.width(100.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Icon(painter = painterResource(id = R.drawable.minus_image), contentDescription = "Togli", tint = Color.White, modifier = Modifier.clickable {
                    if (settimane > 0) {
                        settimane--
                        cpvm.updateTimeForm("settimane", settimane.toString())
                    }
                })
                Text(text = settimane.toString(), color = Color.White, fontSize = 18.sp)
                Icon(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Aggiungi", tint = Color.White, modifier = Modifier.clickable {
                    if (settimane < 3) {
                        settimane++
                        cpvm.updateTimeForm("settimane", settimane.toString())
                    }
                })
            }

            Row (modifier = Modifier.width(90.dp).background(StarCol, RoundedCornerShape(50)).padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Text(text = "Sett", fontSize = 18.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .height(2.dp)
            .fillMaxWidth()
            .background(Color.White))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Row (modifier = Modifier.width(100.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Icon(painter = painterResource(id = R.drawable.minus_image), contentDescription = "Togli", tint = Color.White, modifier = Modifier.clickable {
                    if (giorni > 0){
                        giorni--
                        cpvm.updateTimeForm("giorni", giorni.toString())
                    }
                })
                Text(text = giorni.toString(), color = Color.White, fontSize = 18.sp)
                Icon(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Aggiungi", tint = Color.White, modifier = Modifier.clickable {
                    if (giorni < 6) {
                        giorni++
                        cpvm.updateTimeForm("giorni", giorni.toString())
                    }
                })
            }

            Row (modifier = Modifier.width(90.dp).background(StarCol, RoundedCornerShape(50)).padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Text(text = "Giorni", fontSize = 18.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .height(2.dp)
            .fillMaxWidth()
            .background(Color.White))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Row (modifier = Modifier.width(100.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Icon(painter = painterResource(id = R.drawable.minus_image), contentDescription = "Togli", tint = Color.White, modifier = Modifier.clickable {
                    if (ore > 0){
                        ore--
                        cpvm.updateTimeForm("ore", ore.toString())
                    }
                })
                Text(text = ore.toString(), color = Color.White, fontSize = 18.sp)
                Icon(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Aggiungi", tint = Color.White, modifier = Modifier.clickable {
                    if (ore < 23) {
                        ore++
                        cpvm.updateTimeForm("ore", ore.toString())
                    }
                })
            }

            Row (modifier = Modifier.width(90.dp).background(StarCol, RoundedCornerShape(50)).padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Text(text = "Ore", fontSize = 18.sp, color = Color.White)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun MaterialForm(cpvm: ClassPlayViewModel) {
        var materialDescription by remember { mutableStateOf(cpvm.cosplayFormMaterialDescription.value!!) }
        val keyboardController = LocalSoftwareKeyboardController.current


        TextField(modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(),
            value = materialDescription, placeholder = ({ androidx.compose.material3.Text(text = "Descrivi i materiali utilizzati") }), singleLine = false, maxLines = 5, shape = RoundedCornerShape(10), textStyle = TextStyle(fontSize = 17.sp), onValueChange = {
                if (it.length < 130)
                    materialDescription = it
                cpvm.newMaterialDescriptionCosplayForm(materialDescription)
            }, colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                textColor = Color.Black,
                placeholderColor = Color.Gray,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )
    }
}