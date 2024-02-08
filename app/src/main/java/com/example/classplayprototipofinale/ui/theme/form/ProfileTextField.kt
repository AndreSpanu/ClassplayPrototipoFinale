package com.example.classplayprototipofinale.ui.theme.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classplayprototipofinale.ClassPlayViewModel

class ProfileTextField {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun UsernameTextField(cpvm: ClassPlayViewModel) {

        var username by remember { mutableStateOf(cpvm.formTitle.value) }
        val keyboardController = LocalSoftwareKeyboardController.current


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
            value = username!!, placeholder = ({ Text(text = "Inserisci il titolo del cosplay")}), textStyle = TextStyle(fontSize = 20.sp),
            singleLine = true, onValueChange = { newText ->
                if (newText.length < 15){
                    username = newText.filter { it.isLetterOrDigit() || it == '_' }
                    cpvm.cFormTitle(username!!)
                }
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
    fun BioTextField(cpvm: ClassPlayViewModel) {

        var bio by remember { mutableStateOf(cpvm.formDescription.value!!) }
        val keyboardController = LocalSoftwareKeyboardController.current


        TextField(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
            value = bio, placeholder = ({ Text(text = "Scrivi una tua biografia")}), singleLine = false, maxLines = 5, shape = RoundedCornerShape(10),
            textStyle = TextStyle(fontSize = 17.sp), onValueChange = { newText ->
                if (newText.length < 100)
                    bio = newText
                cpvm.newDescriptionForm(bio)
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
    fun PhoneTextField(cpvm: ClassPlayViewModel) {

        var phoneNumber by remember { mutableStateOf(cpvm.currentTag.value) }
        val keyboardController = LocalSoftwareKeyboardController.current

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                value = phoneNumber ?: "",
                placeholder = ({ Text(text = "Numero di telefono", fontSize = 20.sp) }),
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp),
                onValueChange = { newText ->
                    if (newText.length < 10) {
                        phoneNumber = newText.filter { it.isDigit() }
                        cpvm.compileTag(phoneNumber!!)
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
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
    fun EmailTextField(cpvm: ClassPlayViewModel) {
        var email by remember { mutableStateOf(cpvm.cosplayFormMaterialDescription.value!!) }
        val keyboardController = LocalSoftwareKeyboardController.current

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                value = email,
                placeholder = ({ Text(text = "Inserisci Tag", fontSize = 20.sp) }),
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp),
                onValueChange = { newText ->
                    if (newText.length < 25) {
                        email = newText.filter { it.isLetterOrDigit() || it == '@' || it == '.' || it == '_' }
                        cpvm.newMaterialDescriptionCosplayForm(email)
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
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
}