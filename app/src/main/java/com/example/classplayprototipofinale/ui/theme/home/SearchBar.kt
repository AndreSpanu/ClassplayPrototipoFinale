package com.example.classplayprototipofinale.ui.theme.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.ui.theme.BackgroundCol
import com.example.classplayprototipofinale.ui.theme.DetailsCol
import com.google.firebase.database.DatabaseReference


class SearchBar {

    /** La SearchBar vera e propria presente nella TopBar **/

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun SearchTextField(cpvm: ClassPlayViewModel, ma: MainActivity) {
        var value by remember { mutableStateOf(cpvm.searchingText.value ?: "") }

        cpvm.searchingText.observe(ma) { value = it }

        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Spacer(modifier = Modifier.width(20.dp))
        OutlinedTextField(modifier = Modifier
            .height(55.dp)
            .width(300.dp)
            .onFocusChanged {
                if (it.isFocused) {
                    cpvm.setSearchFocus(focusManager)
                    cpvm.setSearchScreen(true)
                } else {
                    cpvm.setSearchScreen(false)
                    keyboardController?.hide()
                }
            },
            value = value, shape = RoundedCornerShape(50), placeholder = ({ Text(text = "Cerca")}), singleLine = true, trailingIcon = {
                Icon(painter = painterResource(id = R.drawable.lente_viola), contentDescription = "", modifier = Modifier
                    .size(30.dp), tint = DetailsCol)
            }, onValueChange = { newText ->
                value = newText.filter { it.isLetterOrDigit() || it == ' ' || it == '_' }
                cpvm.searchText(newText)
            }, colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                cpvm.addSearchingTag(value)
                value = ""
                cpvm.setSearchScreen(false)
                focusManager.clearFocus()
            })
        )
    }


    /** Lo sfondo su cui compaiono i suggerimenti di ricerca **/

    @Composable
    fun SearchScreen(cpvm: ClassPlayViewModel, ma: MainActivity, user: Users, uDB: DatabaseReference) {
        Column (modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCol)
            .clickable {  }){
            var search by remember { mutableStateOf(cpvm.searchingText.value) }
            var cosplayList by remember { mutableStateOf(cpvm.cosplayList.value) }

            cpvm.cosplayList.observe(ma) { cosplayList = it }
            cpvm.searchingText.observe(ma) { search = it }

            if (user.lastResearch != null) {
                for (tag in user.lastResearch!!) {
                    if (tag.contains(search ?: "", ignoreCase = true)) {
                        Row (modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp, 10.dp, 40.dp, 10.dp)
                            .clickable {
                                cpvm.searchFocus.value?.clearFocus()
                                cpvm.addSearchingTag(tag)
                            }, verticalAlignment = Alignment.CenterVertically){
                            Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Search", tint = DetailsCol, modifier = Modifier.size(25.dp))
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = tag, fontSize = 5.em, color = Color.White)
                        }
                    }
                }
            }

            val tagSearchBar = mutableSetOf<String>()

            if (search != ""){
                for (cosplay in cosplayList!!) {
                    if (user.lastResearch?.contains(cosplay.cosplayName ?: "") == false && cosplay.cosplayName?.contains(search ?: "", ignoreCase = true) == true) {
                        tagSearchBar.add(cosplay.cosplayName)
                    }
                    if (cosplay.tags != null) {
                        for (tag in cosplay.tags!!) {
                            if (tag.contains(search ?: "", ignoreCase = true) && user.lastResearch?.contains(tag) != true && !tagSearchBar.contains(tag.lowercase())) {
                                tagSearchBar.add(tag.lowercase())
                            }
                        }
                    }
                }

                for (tag in tagSearchBar) {
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 10.dp, 40.dp, 10.dp)
                        .clickable {
                            cpvm.searchFocus.value?.clearFocus()
                            cpvm.addSearchingTag(tag)
                            if (user.lastResearch != null) {
                                if (user.lastResearch!!.size > 10) {
                                    user.lastResearch?.removeFirst()
                                }
                            } else {
                                user.lastResearch = mutableListOf<String>()
                            }
                            user.lastResearch!!.add(tag)
                            uDB.child(cpvm.username.value!!)
                                .child("lastResearch")
                                .setValue(
                                    user.lastResearch
                                )
                        }, verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(id = R.drawable.lente_viola), contentDescription = "Search", tint = DetailsCol, modifier = Modifier.size(25.dp))
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = tag, fontSize = 5.em, color = Color.White)
                    }
                }
            }
        }
    }
}