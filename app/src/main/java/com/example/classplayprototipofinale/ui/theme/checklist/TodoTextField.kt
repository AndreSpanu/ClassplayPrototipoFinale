package com.example.classplayprototipofinale.ui.theme.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.models.ToDoStep
import com.example.classplayprototipofinale.screens.LinkType
import com.example.classplayprototipofinale.ui.theme.MyTypography
import com.example.classplayprototipofinale.ui.theme.UploadCol

class TodoTextField {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun TitleTextField(cpvm: ClassPlayViewModel) {

        var title by remember { mutableStateOf(cpvm.formTitle.value) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val context = LocalContext.current


        TextField(modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = Color.White,
                    start = Offset(0f, size.height - 10.dp.toPx()),
                    end = Offset(size.width, size.height - 10.dp.toPx()),
                    strokeWidth = 2.dp.toPx()
                )
            },
            value = title ?: "", placeholder = ({ Text(text = "Inserisci il titolo della ToDo", style = MyTypography.typography.body1, fontSize = 16.sp, color = Color.LightGray)}), textStyle = MyTypography.typography.body1.copy(fontSize = 20.sp),
            singleLine = true, onValueChange = { newText ->
                if (newText.length < 20){
                    title = newText.filter { it.isLetterOrDigit() || it == ' ' || it == '_' }
                    cpvm.cFormTitle(title!!)
                }
                else
                    cpvm.textLimit(context)
            }, colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                textColor = Color.White,
                placeholderColor = Color.Gray,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
                ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun DescriptionTextField(cpvm: ClassPlayViewModel) {

        var description by remember { mutableStateOf(cpvm.formDescription.value ?: "") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val context = LocalContext.current


        TextField(modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
            value = description, placeholder = ({ Text(text = "Inserisci la descrizione del cosplay", style = MyTypography.typography.body1, fontSize = 16.sp, color = Color.Gray)}), singleLine = false, maxLines = 5, shape = RoundedCornerShape(10),
            textStyle = MyTypography.typography.body1.copy(fontSize = 18.sp, color = Color.Black), onValueChange = { newText ->
                if (newText.length < 130)
                    description = newText
                else
                    cpvm.textLimit(context)
                cpvm.newDescriptionForm(description)
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

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun StepTitleTextField(cpvm: ClassPlayViewModel, i: Int, ma: MainActivity) {
        var title by remember { mutableStateOf(cpvm.todoFormTutorial.value?.get("s$i")?.title) }
        val keyboardController = LocalSoftwareKeyboardController.current
        var tutorial = cpvm.todoFormTutorial.value ?: mutableMapOf<String, ToDoStep>()
        val context = LocalContext.current

        cpvm.todoFormTutorial.observe(ma) { tutorial = it
            title = it["s$i"]?.title
        }

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            TextField(modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, size.height - 12.dp.toPx()),
                        end = Offset(size.width, size.height - 12.dp.toPx()),
                        strokeWidth = 2.dp.toPx()
                    )
                },
                value = title?: "", placeholder = ({ Text(text = "Nome del componente",  style = MyTypography.typography.body1.copy(fontSize = 16.sp, color = Color.LightGray))}), singleLine = true, textStyle = MyTypography.typography.body1.copy(fontSize = 20.sp), onValueChange = { newText ->
                    if (newText.length < 20){
                        title = newText.filter { it.isLetterOrDigit() || it == ' ' }
                        tutorial["s$i"]?.title = title
                        cpvm.updateTodoTutorial(tutorial)
                    }
                    else
                        cpvm.textLimit(context)
                }, colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    textColor = Color.White,
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

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun StepDescriptionTextField(cpvm: ClassPlayViewModel, i: Int, ma: MainActivity) {
        var description by remember { mutableStateOf(cpvm.todoFormTutorial.value?.get("s$i")?.description) }
        val keyboardController = LocalSoftwareKeyboardController.current
        var tutorial = cpvm.todoFormTutorial.value ?: mutableMapOf<String, ToDoStep>()
        val context = LocalContext.current

        cpvm.todoFormTutorial.observe(ma) { tutorial = it
            description = it["s$i"]?.description
        }


        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)){
            TextField(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
                value = description ?: "", placeholder = ({ Text(text = "Descrivi cosa vuoi fare in questo step",  style = MyTypography.typography.body1.copy(fontSize = 16.sp, color = Color.Gray))}),
                textStyle = MyTypography.typography.body1.copy(fontSize = 16.sp, color = Color.Black), shape = RoundedCornerShape(16), onValueChange = { newText ->
                    if (newText.length < 200){
                        description = newText
                        tutorial["s$i"]?.description = description
                        cpvm.updateTodoTutorial(tutorial)
                    }
                    else
                        cpvm.textLimit(context)
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

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun StepCosplaySearchTextField(cpvm: ClassPlayViewModel, i: Int, ma: MainActivity) {
        val keyboardController = LocalSoftwareKeyboardController.current
        var appLink by remember { mutableStateOf<String?>("") }

        var currentTypeSelection by remember { mutableStateOf(LinkType.NONE.txt) }

        var tutorial = cpvm.todoFormTutorial.value ?: mutableMapOf<String, ToDoStep>()

        cpvm.todoFormTutorial.observe(ma) { tutorial = it
            currentTypeSelection = it["s$i"]?.linkType.toString()
            if (it["s$i"] != null)
                appLink = it["s$i"]!!.link ?: "" }

        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)){
            Row (modifier = Modifier.fillMaxWidth()){

                var expanded by remember { mutableStateOf(false) }


                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                    androidx.compose.material.TextField(value = currentTypeSelection, onValueChange = {}, readOnly = true, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                        modifier = Modifier.menuAnchor(), shape = RoundedCornerShape(50), colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            textColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),  textStyle = MyTypography.typography.body1.copy(fontSize = 15.sp, color = Color.Black)
                    )

                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        LinkType.values().forEach { type ->
                            if (type != LinkType.NONE) {
                                DropdownMenuItem(text = { Text(text = type.txt,  style = MyTypography.typography.body1.copy(fontSize = 16.sp, color = Color.Black)) }, onClick = {
                                    currentTypeSelection = type.txt
                                    cpvm.setTodoLinkType(i, type.txt)
                                    expanded = false
                                    appLink = ""
                                    tutorial["s$i"]?.link = appLink
                                    cpvm.updateTodoTutorial(tutorial)
                                })
                            }
                        }
                    }
                }
            }

            if (currentTypeSelection == LinkType.APP.txt) {

                Spacer(modifier = Modifier.height(15.dp))

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White, RoundedCornerShape(50))
                    .padding(start = 20.dp)
                    .clickable {
                        cpvm.setShowCosplayTutorialSearch(true)
                    }, verticalAlignment = Alignment.CenterVertically){
                    appLink?.let { Text(text = it,  style = MyTypography.typography.body1.copy(fontSize = 16.sp, color = Color.Black), maxLines = 1) }
                }
            }
            else if (currentTypeSelection == LinkType.LINK.txt) {

                Spacer(modifier = Modifier.height(15.dp))

                appLink?.let {
                    TextField(modifier = Modifier
                        .fillMaxWidth(),
                        value = it, placeholder = null, singleLine = true,  textStyle = MyTypography.typography.body1.copy(fontSize = 16.sp, color = Color.Black), shape = RoundedCornerShape(50), onValueChange = {txt ->
                            appLink = txt
                            tutorial["s$i"]?.link = appLink
                            tutorial["s$i"]?.realAppLink = appLink
                            cpvm.updateTodoTutorial(tutorial)
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            textColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                        })
                    )
                }
            }
            else if (currentTypeSelection == LinkType.PHONE.txt) {

                val context = LocalContext.current
                LaunchedEffect(true) {
                    cpvm.notImplemented(context)
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = Color.White,
                            start = Offset(0f, size.height - 3.dp.toPx()),
                            end = Offset(size.width - 50.dp.toPx(), size.height - 3.dp.toPx()),
                            strokeWidth = 2.dp.toPx()
                        )
                    }, horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "Prova.mp4", fontSize = 15.sp,  style = MyTypography.typography.body1)

                    Column (modifier = Modifier
                        .size(30.dp)
                        .background(UploadCol, RoundedCornerShape(50)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                        Icon(painter = painterResource(id = R.drawable.upload), contentDescription = "Carica un video", modifier = Modifier.size(18.dp), tint = Color.White)
                    }
                }
            }
        }
    }
}