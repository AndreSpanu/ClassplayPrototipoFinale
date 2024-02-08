package com.example.classplayprototipofinale.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.ui.theme.home.CosplayCard
import com.example.classplayprototipofinale.ui.theme.home.SearchBar
import com.example.classplayprototipofinale.ui.theme.home.TagsList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(cpvm: ClassPlayViewModel, ma: MainActivity, cpDB: DatabaseReference, uDB: DatabaseReference, user: Users) {

    val cc = CosplayCard()
    val tl = TagsList()
    val sb = SearchBar()

    var tags by remember { mutableStateOf(setOf<String>()) }
    var zoomCard by remember { mutableStateOf<Cosplay?>(null) }
    var searchScreen by remember { mutableStateOf(false) }

    var cosplaysList by remember { mutableStateOf(listOf<Cosplay>()) }
    var cosplaysListFiltered by remember { mutableStateOf(listOf<Cosplay>()) }

    cpvm.tagList.observe(ma) { tags = it }
    cpvm.zoomCard.observe(ma) { zoomCard = it }
    cpvm.searchScreen.observe(ma) { searchScreen = it }
    cpvm.cosplayListFiltered.observe(ma) { it ->
        cosplaysList = it
        cosplaysListFiltered = cosplaysList.filter {
            val tagList = it.tags
            tagList?.add(it.cosplayName!!)
            tagList?.containsAll(tags) ?: false
        }.toMutableList()
    }
    cpvm.tagList.observe(ma) {
        cosplaysListFiltered = cosplaysList.filter {
            val tagList = it.tags
            tagList?.add(it.cosplayName!!)
            tagList?.containsAll(tags) ?: false
        }.toMutableList()
    }

    /** La lista di Cosplay si aggiorna con il Database **/

    cpDB.addValueEventListener(object :  ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                val newList = mutableListOf<Cosplay>()
                for (snap in snapshot.children){
                    newList.add(snap.getValue(Cosplay::class.java)!!)
                }
                cosplaysList = newList
                cpvm.setCosplayList(newList)
                //if (cpvm.cosplayListFiltered.value!!.isEmpty())
                    cpvm.setCosplayListFiltered()
            }
        }
        override fun onCancelled(error: DatabaseError) {}
    })

    Box(modifier = Modifier
        .fillMaxSize()) {

        /** Il contenuto della HomePage **/

        if (zoomCard == null) {
            Column (modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())){

                tl.TagSlider(tags = tags, cpvm = cpvm)

                FlowRow (
                    Modifier
                        .fillMaxSize()
                        .padding(20.dp, 0.dp), horizontalArrangement = Arrangement.SpaceBetween){
                    for (cosplay in cosplaysListFiltered) {
                        cc.HomeCard( cpvm= cpvm, cosplay = cosplay, cpDB)
                    }
                }
            }
        }

        /** Se abbiamo il focus sulla searchBar, si mostra lo sfondo per la ricerca
         *  Se invece abbiamo zoomato una card, si mostrano i dettagli della stessa **/

        if (searchScreen) {
            sb.SearchScreen(cpvm = cpvm, ma = ma, user = user, uDB)
        }
    }
}