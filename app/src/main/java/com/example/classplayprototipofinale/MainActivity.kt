package com.example.classplayprototipofinale

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.models.ToDo
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.navigation.BottomNavigationBar
import com.example.classplayprototipofinale.navigation.NavigationSetup
import com.example.classplayprototipofinale.ui.theme.main.Background
import com.example.classplayprototipofinale.ui.theme.ComposeBottomNavTheme
import com.example.classplayprototipofinale.ui.theme.CreateDatabase
import com.example.classplayprototipofinale.ui.theme.checklist.ToDoCardOpen
import com.example.classplayprototipofinale.ui.theme.home.CosplayCard
import com.example.classplayprototipofinale.ui.theme.home.PopupMenu
import com.example.classplayprototipofinale.ui.theme.home.SearchBar
import com.example.classplayprototipofinale.ui.theme.home.Video
import com.example.classplayprototipofinale.ui.theme.main.TopBar
import com.example.classplayprototipofinale.ui.theme.main.WarningUp
import com.example.classplayprototipofinale.ui.theme.profile.OthersProfile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class MainActivity : ComponentActivity() {

    private val cpvm by viewModels<ClassPlayViewModel>()

    private lateinit var cpDB: DatabaseReference
    private lateinit var uDB: DatabaseReference
    private lateinit var profileIconsSRef: StorageReference
    private lateinit var cosplaysImgsSRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val th = this
        val sb = SearchBar()
        val bg = Background()
        val tb = TopBar()
        val pm = PopupMenu()
        val cc = CosplayCard()
        val wu = WarningUp()

        /** Classe per creare il database **/
        val cd = CreateDatabase()

        setContent {
            ComposeBottomNavTheme {
                androidx.compose.material.Surface(color = Color.Transparent) {

                    var cardPopup by remember { mutableStateOf(Triple(PopupType.NONE, "", WarningType.NONE)) }
                    var zoomCard by remember { mutableStateOf<Cosplay?>(null) }
                    var todoCard by remember { mutableStateOf<ToDo?>(null) }
                    var profileCard by remember { mutableStateOf<Users?>(null) }

                    cpvm.cardPopup.observe(this) { cardPopup = it }
                    cpvm.zoomCard.observe(this) { zoomCard = it }
                    cpvm.todoCard.observe(this) { todoCard = it }
                    cpvm.otherProfile.observe(this) { profileCard = it }

                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    cpDB = FirebaseDatabase.getInstance().getReference("CosplaysFinal")
                    uDB = FirebaseDatabase.getInstance().getReference("Users")
                    profileIconsSRef = FirebaseStorage.getInstance().getReference("PIcons")
                    cosplaysImgsSRef = FirebaseStorage.getInstance().getReference("CosplaysImgs")

                    var user by remember {  mutableStateOf(Users()) }

                    var data by remember { mutableStateOf("") }
                    val painter = rememberAsyncImagePainter(model = user.profileImgUrl)

                    var showVideo by remember { mutableStateOf(cpvm.stepVideo.value) }
                    cpvm.stepVideo.observe(this) { showVideo = it }

                    var showTodoVideo by remember { mutableStateOf(cpvm.todoStepVideo.value) }
                    cpvm.todoStepVideo.observe(this) { showTodoVideo = it }

                    uDB.child(cpvm.username.value!!).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                user = Users()
                                user = snapshot.getValue(Users::class.java)!!
                                if (cpvm.currentUser.value?.profileImgUrl != user.profileImgUrl) {
                                    data = user.profileImgUrl.toString()
                                }
                                cpvm.setCurrentUser(user)
                                cpvm.setYourTodo(user.yourToDo)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })

                    cpvm.currentUser.observe(this) {
                        if (it.profileImgUrl != null)
                            data = it.profileImgUrl!! }

                    /** Creazione Database **/
                    //cd.cosplay(cpDB, cosplaysImgsSRef)
                    //cd.user(uDB)

                    bg.SetBackground()

                    Scaffold(
                        backgroundColor = Color.Transparent,

                        topBar = { if (currentRoute != null) tb.SetTopBar(currentRoute, sb, cpvm, th, navController) },

                        bottomBar = { BottomNavigationBar(navController, cpvm, painter, cosplaysImgsSRef) }

                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            NavigationSetup(navController, cpvm, th, cpDB, profileIconsSRef, uDB, cosplaysImgsSRef, user)

                            /** Creazione Database **/

                            /*Button(onClick = { /*cd.todo(uDB, cosplaysImgsSRef, user)*/ }) {

                            }*/

                            if (zoomCard != null) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 50.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        cc.ZoomCard(cosplay = zoomCard!!, cpvm = cpvm, cpDB = cpDB, uDB)
                                    }
                                }
                            }

                            if (profileCard != null) {
                                val op = OthersProfile()
                                op.Profile(cpvm = cpvm, cpDB = cpDB, th)
                            }

                            if (todoCard != null) {
                                val tco = ToDoCardOpen()
                                tco.TodoCardInfo(cpvm = cpvm, ma = th, uDB)
                            }

                            if (showVideo != null) {
                                val v = Video()
                                v.ShowVideo(cpvm)
                            }

                            if (showTodoVideo != null) {
                                val v = Video()
                                v.ShowTodoVideo(cpvm)
                            }

                            if (cardPopup.first == PopupType.WARNING)
                                wu.Warning(cpvm = cpvm, navController = navController, cpDB = cpDB, uDB, cosplaysImgsSRef, profileIconsSRef)

                        }
                    }

                    when (cardPopup.first) {

                        PopupType.CARD -> {
                            val context = LocalContext.current
                            LaunchedEffect(true) {
                                cpvm.notImplemented(context)
                            }
                            pm.CardPopup(cpvm)
                        }

                        PopupType.FILTER -> pm.FilterPopup(cpvm = cpvm, this)

                        PopupType.IMPOSTAZIONI -> {
                            val context = LocalContext.current
                            LaunchedEffect(true) {
                                cpvm.notImplemented(context)
                            }
                            pm.ImpostazioniPopup(cpvm = cpvm, this)
                        }

                        PopupType.ERROR -> pm.ErrorPopup(cpvm = cpvm)

                        else -> {}
                    }
                }
            }
        }
    }

    fun uploadImage(type: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        if (type == "profileIcon")
            startActivityForResult(intent, 1)
        else if (type == "cosplayImage")
            startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && data?.data != null) {
            profileIconsSRef.child(cpvm.username.value!!+"Edit").putFile(data.data!!).addOnSuccessListener {
                profileIconsSRef.child(cpvm.username.value!!+"Edit").downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    cpvm.setNewProfileEditImage(data, downloadUrl)
                }
            }
        }
        else if (requestCode == 2 && data?.data != null){
            val id = UUID.randomUUID().toString()
            cosplaysImgsSRef.child(id).putFile(data.data!!).addOnSuccessListener {
                cosplaysImgsSRef.child(id).downloadUrl.addOnSuccessListener {uri ->
                    val downloadUrl = uri.toString()
                    cpvm.addImgForm(downloadUrl, id)
                }
            }
        }
    }
}



/*
In questo esempio viene creata una chiave univoca per ogni impiegato e quando si prende il child per
settargli il valore si passa CPdb.child(id).setValue...

val id = CPdb.push().key!!

CPdb.child("Variabile").setValue(Valore).addOnCompleteListener {
    //Fai qualcosa
}.addOnFailureListener { err ->
    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
}
 */