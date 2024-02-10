package com.example.classplayprototipofinale.ui.theme.form

import androidx.navigation.NavController
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.models.ToDo
import com.example.classplayprototipofinale.models.ToDoStep
import com.example.classplayprototipofinale.models.TutorialStep
import com.example.classplayprototipofinale.navigation.Screen
import com.example.classplayprototipofinale.screens.AppIcons
import com.example.classplayprototipofinale.screens.LinkType
import com.example.classplayprototipofinale.screens.PlusIcon
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CompletableDeferred
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckForm {
    suspend fun cosplayForm(cpvm: ClassPlayViewModel, cpDB: DatabaseReference, navController: NavController, cosplaysImgsSRef: StorageReference): Pair<String?, Int>? {

        val cosplayNameIsValid = CompletableDeferred<Boolean>()

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

        cpDB.child(cpvm.formTitle.value ?: "").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cosplayNameIsValid.complete(!snapshot.exists())
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        val isValid = cosplayNameIsValid.await()

        if (newCosplay.imgUrls!!.isEmpty()) {
            return Pair("Inserisci almeno una foto", 1)
        }
        else if (newCosplay.cosplayName == "") {
            return Pair("Inserisci un titolo per il tuo cosplay", 2)
        }
        else if (!isValid && newCosplay.cosplayName != cpvm.cosplayEdit.value?.cosplayName ) {
            return Pair("${newCosplay.cosplayName} già in uso. Inserire un nome diverso per il tuo cosplay.", 2)
        }
        else if (newCosplay.description == "") {
            return Pair("Inserisci una descrizione per il tuo cosplay", 2)
        }
        else if (newCosplay.tags!!.size < 1) {
            return Pair("Inserisci almeno un Tag", 3)
        }

        if (tutorial != null) {
            for (step in tutorial) {
                if (step.value.icon == PlusIcon.PLUS.url || step.value.componentName == "" || (step.value.link == "" && step.value.description == "")) {
                    println(tutorial.keys)
                    return Pair("A uno o più step del tutorial mancano alcune informazioni, se continui questi step verrano eliminati!\nSei sicuro di voler continuare?", step.key.split("s")[1].toInt() + 8)
                }
            }
            for (step in tutorial) {
                if (step.value.link != null && step.value.linkType == LinkType.LINK.txt) {
                    if (!(step.value.link!!.matches(Regex("http://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) && !(step.value.link!!.matches(Regex("https://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")))) {
                        return Pair("Link non valido in uno o più step, se continui questi link verranno eliminati!\nSei sicuro di voler continuare?", step.key.split("s")[1].toInt() + 8)
                    }
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
                if (!(step.icon == PlusIcon.PLUS.url
                            || step.componentName == ""
                            || ((step.link == ""
                            || (!((step.link ?: "").matches(Regex("http://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) && !((step.link ?: "").matches(Regex("https://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))))
                            && step.linkType == LinkType.LINK.txt) && step.description == ""))) {
                    newTutorial["s" + (step.step?.minus(eliminated)).toString()] = step
                    if (!((step.link ?: "").matches(Regex("http://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) && !((step.link ?: "").matches(Regex("https://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")))){
                        newTutorial["s" + (step.step?.minus(eliminated)).toString()]?.link = ""
                        newTutorial["s" + (step.step?.minus(eliminated)).toString()]?.realAppLink = ""
                    }
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

    suspend fun todoForm(cpvm: ClassPlayViewModel, uDB: DatabaseReference, navController: NavController, cosplaysImgsSRef: StorageReference): Triple<String?, Int, TodoAlertType>? {

        val todoNameIsValid = CompletableDeferred<Boolean>()

        val newTodo = ToDo(cpvm.formTitle.value)

        val tutorial = cpvm.todoFormTutorial.value

        newTodo.description = cpvm.formDescription.value
        newTodo.steps = cpvm.todoFormTutorial.value

        var noLink : Pair<Int, Boolean> = Pair(0, false)

        uDB.child(cpvm.username.value!!).child("yourToDo").child(cpvm.formTitle.value ?: "").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                todoNameIsValid.complete(!snapshot.exists())
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        val isValid = todoNameIsValid.await()

        if (cpvm.formImages.value!!.isEmpty()) {
            return Triple("Inserisci una foto", 1, TodoAlertType.ERROR)
        }
        else if (newTodo.todoTitle == "") {
            return Triple("Inserisci un titolo per la tua ToDo", 2, TodoAlertType.ERROR)
        }
        else if (!isValid && newTodo.todoTitle != cpvm.todoEdit.value?.todoTitle) {
            return Triple("ToDo ${newTodo.todoTitle} già esistente. Inserire un titolo diverso.", 2, TodoAlertType.ERROR)
        }
        else if (newTodo.steps == null) {
            return Triple("Non hai aggiunto alcuno step alla tua ToDo", 2, TodoAlertType.ERROR)
        }
        else if (newTodo.steps!!.isEmpty()) {
            return Triple("Non hai aggiunto alcuno step alla tua ToDo", 2, TodoAlertType.ERROR)
        }

        if (tutorial != null) {
            var counter = 0
            var key = -1
            for (step in tutorial) {
                if (step.value.icon != PlusIcon.PLUS.url && step.value.title != "" && !((step.value.link == "" || (step.value.linkType == LinkType.LINK.txt && !((step.value.link ?: "").matches(Regex("http://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) && !((step.value.link ?: "").matches(Regex("https://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")))) ) && step.value.description == "" ) ) {
                    if (!((step.value.link ?: "").matches(Regex("http://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) && !((step.value.link ?: "").matches(Regex("https://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) && !noLink.second)
                        noLink = Pair(step.key.split("s")[1].toInt() + 3, true)
                    counter++
                }
                else {
                    if (key == -1)
                        key = step.key.split("s")[1].toInt() + 3
                }
            }
            if (counter == 0)
                return Triple("Nessuno step inserito contiene tutte le informazioni necessarie!", 3, TodoAlertType.ERROR)
            else if (noLink.second && key == -1 )
                return  Triple("Link non valido in uno o più step, se continui questi link verranno eliminati!\nSei sicuro di voler continuare?", noLink.first, TodoAlertType.WARNING)
            else if (key != -1)
                return Triple("A uno o più step della tua ToDo list mancano alcune informazioni,\nse continui questi step verrano eliminati!\n\nSei sicuro di vole continuare?", key, TodoAlertType.WARNING)
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
                if (step.icon != PlusIcon.PLUS.url && step.title != "" && !((step.link == "" || (step.linkType == LinkType.LINK.txt && !((step.link ?: "").matches(Regex("http://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) && !((step.link ?: "").matches(Regex("https://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")))) ) && step.description == "" ) ) {

                    newTutorial["s" + (step.step?.minus(eliminated)).toString()] = step
                    newTutorial["s" + (step.step?.minus(eliminated)).toString()]?.step = newTutorial.size

                    if (!((step.link ?: "").matches(Regex("http://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) && !((step.link ?: "").matches(Regex("https://[a-zA-Z0-9._%+-]+\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")))) {
                        newTutorial["s" + (step.step?.minus(eliminated)).toString()]?.link = ""
                    }

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

    suspend fun saveProfile(cpvm: ClassPlayViewModel, uDB: DatabaseReference): String? {

        val usernameIsValid = CompletableDeferred<Boolean>()

        uDB.child(cpvm.formTitle.value ?: "").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usernameIsValid.complete(!snapshot.exists())
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        val isValid = usernameIsValid.await()

        if (cpvm.formImages.value?.isEmpty() == true)
            return "Inserire un'immagine del profilo."

        else if ((cpvm.formTitle.value?.length ?: 0) < 1)
            return "Inserire uno username."

        else if (!isValid && cpvm.formTitle.value!! != cpvm.profileEdit.value!!.username)
            return "Username non disponibile."

        else if ((cpvm.currentTag.value?.length ?: 0) != 10)
            return "Numero di telefono non valido"

        else if (cpvm.cosplayFormMaterialDescription.value?.isNotEmpty() == true) {
            if (!(cpvm.cosplayFormMaterialDescription.value!!.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")))) {
                return "Indirizzo email non valido."
            }
        }

        return null
    }
}

enum class TodoAlertType() {
    WARNING,
    ERROR
}