package com.example.classplayprototipofinale.ui.theme.form

import androidx.navigation.NavController
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.models.ToDo
import com.example.classplayprototipofinale.models.ToDoStep
import com.example.classplayprototipofinale.models.TutorialStep
import com.example.classplayprototipofinale.navigation.Screen
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckForm {
    fun cosplayForm(cpvm: ClassPlayViewModel, cpDB: DatabaseReference, navController: NavController, cosplaysImgsSRef: StorageReference): Pair<String?, Int>? {

        var newCosplay = Cosplay(cpvm.formTitle.value)

        newCosplay.avgReviews = "0"
        newCosplay.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()

        if (cpvm.cosplayEdit.value != null) {
            newCosplay = cpvm.cosplayEdit.value!!
        }

        val tutorial = cpvm.cosplayFormTutorial.value

        newCosplay.username = cpvm.username.value!!
        newCosplay.imgUrls = cpvm.formImages.value
        newCosplay.description = cpvm.formDescription.value
        newCosplay.tags = cpvm.cosplayFormTags.value
        newCosplay.time = cpvm.cosplayFormTime.value
        newCosplay.tutorial = cpvm.cosplayFormTutorial.value
        newCosplay.material = cpvm.cosplayFormMaterialDescription.value

        if (newCosplay.imgUrls!!.isEmpty()) {
            return Pair("Inserisci almeno una foto", 1)
        }
        else if (newCosplay.cosplayName == "") {
            return Pair("Inserisci un titolo per il tuo cosplay", 2)
        }
        else if (newCosplay.description == "") {
            return Pair("Inserisci una descrizione per il tuo cosplay", 2)
        }
        else if (newCosplay.tags!!.size < 1) {
            return Pair("Inserisci almeno un Tag", 3)
        }

        if (tutorial != null) {
            for (step in tutorial) {

                if (step.value.icon == R.drawable.plus_image || step.value.componentName == "" || (step.value.link == "" && step.value.description == "")) {
                    return Pair("A uno o più step del tutorial mancano alcune informazioni, se continui questi step verrano eliminati!\nSei sicuro di vole continuare?", step.key.split("s")[1].toInt() + 8)
                }
            }
        }


        cpvm.saveCosplay(cpDB, newCosplay, cosplaysImgsSRef)

        navController.navigate(Screen.Home.route) {
            navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }

        return null
    }

    fun cosplayFormWarning(cpvm: ClassPlayViewModel): Cosplay {

        var newCosplay = Cosplay(cpvm.formTitle.value)

        newCosplay.avgReviews = "0"
        newCosplay.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()

        if (cpvm.cosplayEdit.value != null) {
            newCosplay = cpvm.cosplayEdit.value!!
        }

        val tutorial = cpvm.cosplayFormTutorial.value
        val newTutorial : MutableMap<String, TutorialStep> = mutableMapOf()

        newCosplay.username = cpvm.username.value!!
        newCosplay.imgUrls = cpvm.formImages.value
        newCosplay.description = cpvm.formDescription.value
        newCosplay.tags = cpvm.cosplayFormTags.value
        newCosplay.time = cpvm.cosplayFormTime.value
        newCosplay.material = cpvm.cosplayFormMaterialDescription.value

        if (tutorial != null) {
            var eliminated = 1
            val newOrder = tutorial.values.sortedBy { it.step }
            for (step in newOrder) {
                if (!(step.icon == R.drawable.plus_image || step.componentName == "" || (step.link == "" && step.description == ""))) {
                    newTutorial["s" + (step.step?.minus(eliminated)).toString()] = step
                    newTutorial["s" + (step.step?.minus(eliminated)).toString()]?.step = newTutorial.size
                }
                else
                    eliminated++
            }
        }

        if (newTutorial.isNotEmpty())
            newCosplay.tutorial = newTutorial

        return newCosplay
    }

    fun todoForm(cpvm: ClassPlayViewModel, uDB: DatabaseReference, navController: NavController, cosplaysImgsSRef: StorageReference): Pair<String?, Int>? {

        val newTodo = ToDo(cpvm.formTitle.value)

        val tutorial = cpvm.todoFormTutorial.value

        newTodo.description = cpvm.formDescription.value
        newTodo.steps = cpvm.todoFormTutorial.value

        if (cpvm.formImages.value!!.isEmpty()) {
            return Pair("Inserisci una foto", 1)
        }
        else if (newTodo.todoTitle == "") {
            return Pair("Inserisci un titolo per la tua ToDo", 2)
        }
        else if (newTodo.steps == null) {
            return Pair("Non hai aggiunto alcuno step alla tua ToDo", 2)
        }
        else if (newTodo.steps!!.isEmpty()) {
            return Pair("Non hai aggiunto alcuno step alla tua ToDo", 2)
        }

        if (tutorial != null) {
            var counter = 0
            var key = -1
            for (step in tutorial) {
                if (!(step.value.icon == R.drawable.plus_image || step.value.title == "" || (step.value.link == "" && step.value.description == ""))) {
                    counter++
                }
                else {
                    if (key == -1)
                        key = step.key.split("s")[1].toInt() + 3
                }
            }
            if (counter == 0)
                return Pair("Nessuno step inserito contiene tutte le informazioni necessarie!", 2)
            else if (key != -1)
                return Pair("A uno o più step della tua ToDo list mancano alcune informazioni,\nse continui questi step verrano eliminati!\n\nSei sicuro di vole continuare?", key)
        }

        newTodo.img = cpvm.formImages.value

        cpvm.saveTodo(uDB, newTodo, cosplaysImgsSRef)

        navController.navigate(Screen.Checklist.route) {
            navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }

        return null
    }

    fun todoFormWarning(cpvm: ClassPlayViewModel): ToDo {

        val newTodo = ToDo(cpvm.formTitle.value)

        val tutorial = cpvm.todoFormTutorial.value
        val newTutorial : MutableMap<String, ToDoStep> = mutableMapOf()

        newTodo.description = cpvm.formDescription.value

        if (tutorial != null) {
            var eliminated = 1
            val newOrder = tutorial.values.sortedBy { it.step }
            for (step in newOrder) {
                if (!(step.icon == R.drawable.plus_image || step.title == "" || (step.link == "" && step.description == ""))) {
                    newTutorial["s" + (step.step?.minus(eliminated)).toString()] = step
                    newTutorial["s" + (step.step?.minus(eliminated)).toString()]?.step = newTutorial.size
                }
                else
                    eliminated++
            }
        }

        newTodo.img = cpvm.formImages.value

        if (newTutorial.isNotEmpty())
            newTodo.steps = newTutorial

        return newTodo
    }

    fun saveProfile(cpvm: ClassPlayViewModel, uDB: DatabaseReference, profileIconSR: StorageReference) {
        uDB.child(cpvm.username.value!!).child("emailAddress").setValue(cpvm.cosplayFormMaterialDescription.value)
        uDB.child(cpvm.username.value!!).child("phoneNumber").setValue(cpvm.currentTag.value)
        uDB.child(cpvm.username.value!!).child("bio").setValue(cpvm.formDescription.value)
        uDB.child(cpvm.username.value!!).child("username").setValue(cpvm.formTitle.value)

        if (cpvm.currentUser.value?.profileImgUrl != cpvm.profileEdit.value!!.profileImgUrl) {
            profileIconSR.child(cpvm.username.value!!).putFile(cpvm.profileEditImg.value!!)
            profileIconSR.child(cpvm.username.value!!).downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                uDB.child(cpvm.username.value!!).child("profileImgUrl").setValue(downloadUrl)
            }
            profileIconSR.child(cpvm.username.value!!+"Edit").delete()
        }

    }
}