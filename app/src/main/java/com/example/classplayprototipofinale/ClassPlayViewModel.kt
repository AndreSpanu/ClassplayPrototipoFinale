package com.example.classplayprototipofinale

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.focus.FocusManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.models.ToDo
import com.example.classplayprototipofinale.models.ToDoStep
import com.example.classplayprototipofinale.models.TutorialStep
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.screens.LinkType
import com.example.classplayprototipofinale.screens.PlusIcon
import com.example.classplayprototipofinale.ui.theme.form.CheckForm
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class ClassPlayViewModel: ViewModel() {
    //Home Screen, Form Screens
    private val _cosplayList = MutableLiveData<List<Cosplay>>().also { it.value = listOf<Cosplay>() }           /** Lista di cosplay visualizzata nella Home e usata per la ricerca dei tutorial step **/
    private val _cosplayListFiltered = MutableLiveData<List<Cosplay>>().also { it.value = listOf<Cosplay>() }   /** Versione filtrata della cosplay list **/
    private val _cardPopup = MutableLiveData<Triple<PopupType, String, WarningType>>().also { it.value = Triple(PopupType.NONE, "", WarningType.NONE) }
    private val _destination = MutableLiveData<String?>().also { it.value = null }                              /** Destinazione dove viene reindirizzato l'utente dopo aver confermato l'Annulla **/
    private val _cosplayEdit = MutableLiveData<Cosplay?>().also { it.value = null }                             /** Le informazioni relative al cosplay in fase di modifica **/
    private val _todoEdit = MutableLiveData<ToDo?>().also { it.value = null }                                   /** Le informazioni relative alla checklist in fase di modifica **/
    private val _todoCard = MutableLiveData<ToDo?>().also { it.value = null }
    private val _cosplayEditImages = MutableLiveData<MutableMap<String, String>?>().also { it.value = null }

    val cosplayList: LiveData<List<Cosplay>> = _cosplayList
    val cosplayListFiltered: LiveData<List<Cosplay>> = _cosplayListFiltered
    val cardPopup: LiveData<Triple<PopupType, String, WarningType>> = _cardPopup
    val destination: LiveData<String?> = _destination
    val todoEdit: LiveData<ToDo?> = _todoEdit
    val todoCard: LiveData<ToDo?> = _todoCard
    val cosplayEdit: LiveData<Cosplay?> = _cosplayEdit

    //Home Screen
    private val _searchingText = MutableLiveData<String>().also { it.value = "" }                               /** Il testo inserito nella barra di ricerca **/
    private val _zoomCard = MutableLiveData<Cosplay?>().also { it.value = null }                                 /** La card selezionata che viene aperta **/
    private val _otherProfile = MutableLiveData<Users?>().also { it.value = null }
    private val _tagList = MutableLiveData<MutableSet<String>>().also { it.value = mutableSetOf() }             /** La lista dei tag di filtraggio ricerca **/
    private val _searchScreen = MutableLiveData<Boolean>().also { it.value = false }                            /** La schermata di ricerca per tag **/
    private val _eliminate = MutableLiveData<Boolean>().also { it.value = false }
    private val _searchFocus = MutableLiveData<FocusManager>()                                                  /** Il focus sul textField della barra di ricerca **/
    private val _stepVideo = MutableLiveData<TutorialStep?>().also { it.value = null }                          /** Il video da visualizzare **/
    private val _todoStepVideo = MutableLiveData<ToDoStep?>().also { it.value = null }

    val searchingText: LiveData<String> = _searchingText;
    val zoomCard: LiveData<Cosplay?> = _zoomCard;
    val otherProfile: LiveData<Users?> = _otherProfile;
    val tagList: LiveData<MutableSet<String>> = _tagList;
    val searchScreen: LiveData<Boolean> = _searchScreen
    val eliminate: LiveData<Boolean> = _eliminate
    val searchFocus: LiveData<FocusManager> = _searchFocus
    val stepVideo: LiveData<TutorialStep?> = _stepVideo
    val todoStepVideo: LiveData<ToDoStep?> = _todoStepVideo

    //Profile Screen
    private val _username = MutableLiveData<String>().also { it.value = "tester" }                              /** Lo username del currentUser **/
    private val _favoriteOpen = MutableLiveData<Boolean>().also { it.value = false }                            /** La schermata dei preferiti dall'utente **/
    private val _currentUser = MutableLiveData<Users>()                                                         /** Il currentUser **/
    private val _profileEdit = MutableLiveData<Users?>()                                                         /** Il profilo in modifica **/
    private val _profileEditImg = MutableLiveData<Uri?>()                                                       /** La nuova immagine del profilo **/

    val username: LiveData<String> = _username;
    val favoriteOpen: LiveData<Boolean> = _favoriteOpen;
    val currentUser: LiveData<Users> = _currentUser
    val profileEdit: LiveData<Users?> = _profileEdit

    //Cosplay Form
    private val _formTitle = MutableLiveData<String>().also { it.value = "" }                                   /** Il title comune ai form di todo e cosplay **/
    private val _currentTag = MutableLiveData<String>().also { it.value = "" }                                  /** La lista dei tag del form **/
    private val _formDescription = MutableLiveData<String>().also { it.value = "" }                             /** La descrizione comune ai form di todo e cosplay **/
    private val _cosplayFormAvgReviews = MutableLiveData<String>().also { it.value = "0.0" }                    /** La media delle recensioni del cosplay (nel form) **/
    private val _cosplayFormMaterialDescription = MutableLiveData<String>().also { it.value = "" }              /** La descrizione dei materiali del cosplay nel form **/
    private val _cosplayFormTags = MutableLiveData<MutableList<String>>().also { it.value = mutableListOf<String>() }       /** I tag selezionati per il cosplaydw **/
    private val _cosplayFormTutorial = MutableLiveData<MutableMap<String, TutorialStep>>().also { it.value = mutableMapOf<String, TutorialStep>() }     /** Il tutorial del form cosplay **/
    private val _formImages = MutableLiveData<MutableMap<String, String>>().also { it.value = mutableMapOf<String, String>() }                   /** Le immagini inserite nel form **/
    private val _cosplayFormTime = MutableLiveData<MutableMap<String, Int>>().also { it.value = mutableMapOf<String, Int>("anni" to 0, "mesi" to 0, "settimane" to 0, "giorni" to 0, "ore" to 0) }      /** Inserimento del tempo di realizzazione **/

    val formTitle: LiveData<String> = _formTitle;
    val currentTag: LiveData<String> = _currentTag;
    val formDescription: LiveData<String> = _formDescription;
    val cosplayFormMaterialDescription: LiveData<String> = _cosplayFormMaterialDescription;
    val cosplayFormTags: LiveData<MutableList<String>> = _cosplayFormTags;
    val cosplayFormTutorial: LiveData<MutableMap<String, TutorialStep>> = _cosplayFormTutorial;
    val formImages: LiveData<MutableMap<String, String>> = _formImages;
    val cosplayFormTime: LiveData<MutableMap<String, Int>> = _cosplayFormTime;

    //ToDo Form
    private val _todoFormTutorial = MutableLiveData<MutableMap<String, ToDoStep>>().also { it.value = mutableMapOf<String, ToDoStep>() }        /** Il tutorial del form todo **/

    val todoFormTutorial: LiveData<MutableMap<String, ToDoStep>> = _todoFormTutorial;

    //Tutorial
    private val _currentStep = MutableLiveData<Int>().also { it.value = 1 }                                     /** Lo step corrente del form **/
    private val _totalSteps = MutableLiveData<Int>().also { it.value = 1 }                                      /** Il numero totale di stel del form **/
    private val _showIconGrid = MutableLiveData<Boolean>().also { it.value = false }                            /** La griglia di scelta dell'icona dello step **/
    private val _showCosplayTutorialSearch = MutableLiveData<Boolean>().also { it.value = false }               /** La griglia di selezione del tutorial del cosplay **/
    private val _currentCosplaySearch = MutableLiveData<String>().also { it.value = "" }                        /** La ricerca nella griglia dei cosplay **/
    private val _stepForTodo = MutableLiveData<TutorialStep>().also { it.value = TutorialStep() }

    val currentStep: LiveData<Int> = _currentStep;
    val totalSteps: LiveData<Int> = _totalSteps;
    val showIconGrid: LiveData<Boolean> = _showIconGrid;
    val showCosplayTutorialSearch: LiveData<Boolean> = _showCosplayTutorialSearch;
    val currentCosplaySearch: LiveData<String> = _currentCosplaySearch;

    //Checklist Screen
    private val _done = MutableLiveData<Boolean>().also { it.value = false }                                                        /** Scelta quali todo visualizzare **/
    private val _yourTodo = MutableLiveData<MutableMap<String, ToDo>?>().also { it.value = mutableMapOf<String, ToDo>() }           /** Le todo del current user **/

    val done: LiveData<Boolean> = _done;
    val yourTodo: LiveData<MutableMap<String, ToDo>?> = _yourTodo;

    //Filter Popup
    private val _filter = MutableLiveData<Triple<Boolean, Int, Int>>().also { it.value = Triple(true, 0, 5) }

    val filter: LiveData<Triple<Boolean, Int, Int>> = _filter;



    /*********************************************************** FORM GENERALE **********************************************************************/

    /** Aggiunta di un'immagine al form **/
    fun addImgForm(imgUrl: String, id: String) {
        val newImgsList = _formImages.value
        newImgsList!![id] = imgUrl
        _formImages.value = newImgsList
    }

    /** Rimozione di un'immagine dal form **/
    fun removeImgForm(key: String) {
        val newImgsList = _formImages.value
        newImgsList!!.remove(key)
        _formImages.value = newImgsList
    }

    /** Aggiunta del titolo al form **/
    fun cFormTitle(title: String) {
        _formTitle.value = title
    }

    /** Aggiunta della descrizione al form **/
    fun newDescriptionForm(description: String) {
        _formDescription.value = description
    }

    /** Aggiornamento degli step totali come aumento **/
    fun updateTotalSteps(int: Int) {
        _totalSteps.value = _currentStep.value?.plus(int)
    }

    /** Aggiornamento degli step totali **/
    fun setTotalSteps(int: Int) {
        _totalSteps.value = int
    }

    /** Aggiornamento dello step corrente **/
    fun setCurrentStep (int: Int) {
        _currentStep.value = int
    }

    /** Aggiornamento dell'attuale ricerca di tutorial step dai cosplay **/
    fun updateCosplaySearch(txt: String) {
        _currentCosplaySearch.value = txt
    }

    /** Mostra la griglia delle icone per gli step **/
    fun setShowIconGrid(bool: Boolean) {
        _showIconGrid.value = bool
    }

    /** Mostra la griglia dei tutorial step **/
    fun setShowCosplayTutorialSearch(bool: Boolean) {
        _showCosplayTutorialSearch.value = bool
    }

    /** Seleziona la destinazione di reindirizzamento una volta confermata l'uscita dal form **/
    fun setDestination(route: String?) {
        _destination.value = route
    }

    /** Reset del form **/
    fun clearForm () {
        _currentStep.value = 1
        _totalSteps.value = 2
        _todoFormTutorial.value?.clear()
        _formTitle.value = ""
        _currentTag.value = ""
        _cosplayFormTags.value?.clear()
        _formDescription.value = ""
        _cosplayFormTime.value = mutableMapOf<String, Int>("anni" to 0, "mesi" to 0, "settimane" to 0, "giorni" to 0, "ore" to 0)
        _formImages.value = mutableMapOf<String, String>()
        _cosplayFormMaterialDescription.value = ""
        _cosplayFormTutorial.value = mutableMapOf<String, TutorialStep>()
        setZoomCard(null)
        setOtherProfile(null)
        setFavoriteOpen(false)
        setTodoCard(null)
    }

    /*********************************************************** COSPLAY FORM **********************************************************************/

    /** Aggiunga della descrizione dei materiali **/
    fun newMaterialDescriptionCosplayForm(description: String) {
        _cosplayFormMaterialDescription.value = description
    }

    /** Aggiunta di un tag al cosplay nel form **/
    fun addTagCosplayForm(tag: String) {
        if (tag != "") {
            val set = mutableSetOf<String>()
            for (t in _cosplayFormTags.value!!)
                set.add(t)
            set.add(tag)
            _cosplayFormTags.value = set.toMutableList()
        }
    }

    /** Rimozione di un tag al cosplay nel form **/
    fun removeTagCosplayForm(tag: String) {
        val set = mutableListOf<String>()
        for (t in _cosplayFormTags.value!!)
        {
            if (t != tag)
                set.add(t)
        }
        _cosplayFormTags.value = set
    }

    /** Aggiornamento del tempo di realizzazione del cosplay **/
    fun updateTimeForm(key: String, valore: String) {
        val newTimeMap = _cosplayFormTime.value
        newTimeMap!![key] = valore.toInt()
        _cosplayFormTime.value = newTimeMap
    }

    /** Imposta il tipo di link inserito per il Cosplay Form **/
    fun setLinkType(index: Int, linkType: String) {
        _cosplayFormTutorial.value?.get("s$index")?.linkType = linkType
    }

    /** Aggiunta di un nuovo step del tutorial **/
    fun newTutorialStep() {
        val step = TutorialStep(_cosplayFormTutorial.value!!.size + 1)

        step.icon = PlusIcon.PLUS.url
        step.description = ""
        step.componentName = ""
        step.link = ""
        step.linkType = LinkType.NONE.txt

        _cosplayFormTutorial.value!!["s"+_cosplayFormTutorial.value!!.size.toString()] = step
    }

    /** Aggiorna il tutorial del form del cosplay **/
    fun updateTutorial(tutorial: MutableMap<String, TutorialStep>) {
        _cosplayFormTutorial.value = tutorial
    }

    /** Cambia l'ordine degli step del tutorial **/
    fun changeStepPosition(previousPosition: Int, newPosition: Int) {
        val newMap = mutableMapOf<String, TutorialStep>()

        for( element in _cosplayFormTutorial.value!!) {
            newMap[element.key] = element.value
        }

        var find = 0

        if (newPosition < previousPosition) {

            newMap["s$newPosition"] = _cosplayFormTutorial.value!!["s$previousPosition"]!!

            newMap["s$newPosition"]?.step = newPosition + 1

            for (i in newPosition + 1..newMap.size) {
                if (i - 1 != previousPosition) {
                    newMap["s"+(i - find).toString()] = _cosplayFormTutorial.value!!["s"+(i-1).toString()]!!
                    newMap["s"+(i - find).toString()]?.step = i - find + 1
                }
                else {
                    find = 1
                }
            }
        }
        else{
            for (i in previousPosition until newMap.size) {
                if (i != newPosition) {
                    newMap["s$i"] = _cosplayFormTutorial.value!!["s"+(i+1 - find).toString()]!!
                    newMap["s$i"]?.step = i + 1
                }
                else {
                    newMap["s$i"] = _cosplayFormTutorial.value!!["s$previousPosition"]!!
                    newMap["s$i"]?.step = i + 1
                    find = 1
                }
            }
        }

        _cosplayFormTutorial.value = newMap
    }

    /*********************************************************** CHECKLIST FORM **********************************************************************/

    /** Nuovo step nel form todo **/
    fun newTodoStep() {
        val step = ToDoStep(_todoFormTutorial.value!!.size + 1)

        step.icon = PlusIcon.PLUS.url
        step.description = ""
        step.title = ""
        step.link = ""
        step.linkType = LinkType.NONE.txt
        step.completed = false

        _todoFormTutorial.value!!["s"+_todoFormTutorial.value!!.size.toString()] = step
    }

    /** Aggiornamento degli step della todo **/
    fun updateTodoTutorial(tutorial: MutableMap<String, ToDoStep>) {
        _todoFormTutorial.value = tutorial
    }

    fun removeCosplayStep() {
        val step = _currentStep.value?.minus(8)
        val newMap = mutableMapOf<String, TutorialStep>()
        _eliminate.value = true

        for( element in _cosplayFormTutorial.value!!) {
            newMap[element.key] = element.value
        }

        if ((_cosplayFormTutorial.value?.size ?: 0) > 1) {
            for (i in step!! until _cosplayFormTutorial.value!!.size - 1) {
                newMap["s$i"] = _cosplayFormTutorial.value!!["s${i+1}"]!!
            }
            _cosplayFormTutorial.value = newMap
            _totalSteps.value = _totalSteps.value!! - 1
        }
        else {
            _cosplayFormTutorial.value?.clear()
            newTutorialStep()
        }
    }

    fun removeTodoStep() {
        val step = _currentStep.value?.minus(3)
        val newMap = mutableMapOf<String, ToDoStep>()
        _eliminate.value = true

        for( element in _todoFormTutorial.value!!) {
            newMap[element.key] = element.value
        }

        if ((_todoFormTutorial.value?.size ?: 0) > 1) {
            for (i in step!! until _todoFormTutorial.value!!.size - 1) {
                newMap["s$i"] = _todoFormTutorial.value!!["s${i+1}"]!!
            }
            _todoFormTutorial.value = newMap
            _totalSteps.value = _totalSteps.value!! - 1
        }
        else {
            _todoFormTutorial.value?.clear()
            newTutorialStep()
        }
    }

    /** Aggiornamento dell'ordine degli step della todo **/
    fun changeTodoStepPosition(previousPosition: Int, newPosition: Int) {
        val newMap = mutableMapOf<String, ToDoStep>()

        for( element in _todoFormTutorial.value!!) {
            newMap[element.key] = element.value
        }

        var find = 0

        if (newPosition < previousPosition) {

            newMap["s$newPosition"] = _todoFormTutorial.value!!["s$previousPosition"]!!

            newMap["s$newPosition"]?.step = newPosition + 1

            for (i in newPosition + 1..newMap.size) {
                if (i - 1 != previousPosition) {
                    newMap["s"+(i - find).toString()] = _todoFormTutorial.value!!["s"+(i-1).toString()]!!
                    newMap["s"+(i - find).toString()]?.step = i - find + 1
                }
                else {
                    find = 1
                }
            }
        }
        else{
            for (i in previousPosition until newMap.size) {
                if (i != newPosition) {
                    newMap["s$i"] = _todoFormTutorial.value!!["s"+(i+1 - find).toString()]!!
                    newMap["s$i"]?.step = i + 1
                }
                else {
                    newMap["s$i"] = _todoFormTutorial.value!!["s$previousPosition"]!!
                    newMap["s$i"]?.step = i + 1
                    find = 1
                }
            }
        }

        _todoFormTutorial.value = newMap
    }

    /** Aggiornamento del tipo di link inserito **/
    fun setTodoLinkType(index: Int, linkType: String) {
        _todoFormTutorial.value?.get("s$index")?.linkType = linkType
    }

    /** Salvataggio della todo **/
    fun saveTodo (uDB: DatabaseReference, todo: ToDo, cosplaysImgsSRef: StorageReference) {
        val userUpdate = _currentUser.value

        if (userUpdate!!.yourToDo == null)
            userUpdate.yourToDo = mutableMapOf<String, ToDo>(todo.todoTitle!! to todo)
        else
            userUpdate.yourToDo?.set(todo.todoTitle!!, todo)

        uDB.child(_username.value!!).setValue(userUpdate)

        if (_todoEdit.value != null) {
            if (_todoEdit.value!!.todoTitle!! != todo.todoTitle)
                uDB.child(_username.value!!).child("yourToDo").child(_todoEdit.value!!.todoTitle!!).removeValue()

            if ((_formImages.value?.keys?.first() ?: "a") != (_todoEdit.value!!.img?.keys?.first() ?: "a"))
                cosplaysImgsSRef.child(_todoEdit.value!!.img!!.keys.first()).delete()

            _todoEdit.value = null
        }

        _formImages.value = mutableMapOf<String, String>()

        clearForm()
    }

    /** Imposta la todo da modifiicare **/
    fun setTodoEdit (todo: ToDo?) {
        if (todo != null) {
            _todoEdit.value = ToDo(todo.todoTitle)
            _todoEdit.value?.img = todo.img
            _todoEdit.value?.description = todo.description
            _todoEdit.value?.steps = todo.steps
            _formTitle.value = todo.todoTitle
            _formDescription.value = todo.description
            _formImages.value = todo.img!!.toMutableMap()
            _todoFormTutorial.value = todo.steps
            _currentStep.value = 1
            _totalSteps.value = todo.steps!!.size + 2
            setTodoCard(null)
        }
    }

    /** Salvataggio della todo eliminando gli step non correttamente compilati **/
    fun saveTodoWarning (uDB: DatabaseReference, cosplaysImgsSRef: StorageReference) {
        val cf = CheckForm()
        val todo = cf.todoFormWarning(this)
        val userUpdate = _currentUser.value

        if (userUpdate!!.yourToDo == null)
            userUpdate.yourToDo = mutableMapOf<String, ToDo>(todo.todoTitle!! to todo)
        else
            userUpdate.yourToDo?.set(todo.todoTitle!!, todo)

        uDB.child(_username.value!!).setValue(userUpdate)

        if (_todoEdit.value != null) {
            uDB.child(_username.value!!).child("yourToDo").child(_todoEdit.value!!.todoTitle!!).removeValue()


            if ((_formImages.value?.keys?.first() ?: "a") != (_todoEdit.value!!.img?.keys?.first() ?: "a"))
                cosplaysImgsSRef.child(_todoEdit.value!!.img!!.keys.first()).delete()

            _todoEdit.value = null
        }

        _formImages.value = mutableMapOf<String, String>()

        clearForm()
    }

    /** Salvataggio del cosplay creato nel form **/
    fun saveCosplay (CPdb: DatabaseReference, cosplay: Cosplay, cosplaysImgsSRef: StorageReference) {

        if (_cosplayEdit.value != null) {
            CPdb.child(_cosplayEdit.value!!.cosplayName!!).removeValue()

            if (_cosplayEdit.value!!.imgUrls != null) {
                for (img in _cosplayEditImages.value!!) {
                    if (!(_formImages.value!!.keys.contains(img.key))) {
                        cosplaysImgsSRef.child(img.key).delete()
                    }
                }

                _cosplayEditImages.value = null
            }
        }

        CPdb.child(cosplay.cosplayName!!).setValue(cosplay)
        _formImages.value = mutableMapOf<String, String>()

        clearForm()
    }

    /** Salvataggio di un cosplay eliminando gli step non correttamente compilati **/
    fun saveCosplayWarning (CPdb: DatabaseReference, cosplaysImgsSRef: StorageReference) {
        val cf = CheckForm()
        val cosplay = cf.cosplayFormWarning(this)

        if (_cosplayEdit.value != null) {
            CPdb.child(_cosplayEdit.value!!.cosplayName!!).removeValue()

            if (_cosplayEdit.value!!.imgUrls != null) {
                for (img in _cosplayEditImages.value!!) {
                    if (!(_formImages.value!!.keys.contains(img.key))) {
                        cosplaysImgsSRef.child(img.key).delete()
                    }
                }

                _cosplayEditImages.value = null
            }
        }

        CPdb.child(cosplay.cosplayName!!).setValue(cosplay)
        _formImages.value = mutableMapOf<String, String>()

        clearForm()
    }

    fun newTodoFromStep() {
        val step = _stepForTodo.value!!
        newTodoStep()
        _todoFormTutorial.value!!.values.first().title = step.componentName
        _todoFormTutorial.value!!.values.first().description = step.description
        _todoFormTutorial.value!!.values.first().linkType = step.linkType
        _todoFormTutorial.value!!.values.first().link = step.link
        _todoFormTutorial.value!!.values.first().realAppLink = step.realAppLink
        _todoFormTutorial.value!!.values.first().icon = step.icon
        _currentStep.value = 3
        _totalSteps.value = 3
        _zoomCard.value = null
    }

    /*********************************************************** SEARCH SCREEN **********************************************************************/

    /** Aggiornamento della ricerca tramite search bar **/
    fun searchText(text: String) {
        _searchingText.value = text
    }

    /** Aggiunta di un tag di filtraggio della ricerca **/
    fun addSearchingTag(tag: String) {
        val set = mutableSetOf<String>()
        for (t in _tagList.value!!)
            set.add(t)
        set.add(tag)
        _tagList.value = set
        _searchingText.value = ""
    }

    /** Rimozione di un tag di filtraggio della ricerca **/
    fun removeSearchingTag(tag: String) {
        val set = mutableSetOf<String>()
        for (t in _tagList.value!!)
        {
            if (t != tag)
                set.add(t)
        }
        _tagList.value = set
    }

    /** Imposta il focus sulla search bar **/
    fun setSearchFocus(focus: FocusManager) {
        _searchFocus.value = focus
    }

    /** Mostra la schermata di ricerca **/
    fun setSearchScreen(bool: Boolean) {
        _searchScreen.value = bool
        if (!bool) {
            _searchingText.value = ""
        }
    }

    /*********************************************************** HOME SCREEN **********************************************************************/

    fun setCardPopup(popupType: PopupType, message: String = "", warningType: WarningType = WarningType.NONE) {
        _cardPopup.value = Triple(popupType, message, warningType)
    }

    /** Aggiorna la lista di cosplay da visualizzare **/
    fun setCosplayList(newCosplayList: MutableList<Cosplay>){
        _cosplayList.value = newCosplayList
    }

    /** Imposta la card di cui vedere le informazioni **/
    fun setZoomCard(card: Cosplay?) {
        _zoomCard.value = card
        setOtherProfile(null)
    }

    fun setTodoCard(todo: ToDo?) {
        _todoCard.value = todo
    }

    fun setOtherProfile(username: String?, uDB: DatabaseReference? = null) {
        if (username == null)
            _otherProfile.value = null
        else {
            _zoomCard.value = null
            uDB?.child(username)?.get()?.addOnCompleteListener { task ->
                _otherProfile.value = task.result.getValue(Users::class.java)
            }
        }
    }

    /** Aggiorna il tag in scrittura nel TextField **/
    fun compileTag(tag: String) {
        _currentTag.value = tag
    }

    /*********************************************************** PROFILE SCREEN **********************************************************************/

    /** Mostra la sezione dei preferiti **/
    fun setFavoriteOpen(bool: Boolean) {
        _favoriteOpen.value = bool
    }

    /** Imposta lo user corrente **/
    fun setCurrentUser(user: Users?) {
        if (user != null) {
            _currentUser.value = Users(user.username)
            _currentUser.value!!.emailAddress = user.emailAddress
            _currentUser.value!!.phoneNumber = user.phoneNumber
            _currentUser.value!!.profileImgUrl = user.profileImgUrl
            _currentUser.value!!.lastResearch = user.lastResearch
            _currentUser.value!!.yourToDo = user.yourToDo
            _currentUser.value!!.bio = user.bio
        }

    }

    /** Aggiorna l'immagine del profilo **/

    fun setNewProfileEditImage(data: Intent?, url: String) {
        _profileEditImg.value = data!!.data!!
        val newUser = _profileEdit.value
        _profileEdit.value = Users(newUser!!.username)
        newUser.profileImgUrl = url
        _profileEdit.value = newUser!!
    }

    /** Crea una copia del profilo corrente per le modifiche **/
    fun setNewProfileEdit(user: Users) {

        _profileEdit.value = Users(user.username)
        _profileEdit.value!!.emailAddress = user.emailAddress
        _profileEdit.value!!.phoneNumber = user.phoneNumber
        _profileEdit.value!!.profileImgUrl = user.profileImgUrl
        _profileEdit.value!!.lastResearch = user.lastResearch
        _profileEdit.value!!.yourToDo = user.yourToDo
        _profileEdit.value!!.bio = user.bio
    }

    /*********************************************************** CHECKLIST SCREEN **********************************************************************/

    /** Imposta la visualizzazione su Done/Todo **/
    fun setDone(bool: Boolean) {
        _done.value = bool
    }

    /** Aggiorna le todo del current user **/
    fun setYourTodo(todo: MutableMap<String, ToDo>?) {
        if (todo != null)
            _yourTodo.value = todo
        else
            _yourTodo.value = mutableMapOf()

    }

    /*********************************************************** FILTER POPUP **********************************************************************/

    fun setFilter(asc: Boolean = _filter.value!!.first, min: Int = _filter.value!!.second, max: Int = _filter.value!!.third) {
        _filter.value = Triple(asc, min, max)
    }

    /** Filtraggio della lista di cosplay da visualizzare **/
    fun filterCosplayList() {
        val (asc, minVal, maxVal) = _filter.value!!

        val firstFilter = _cosplayList.value?.filter { it ->
            val tagList = it.tags
            tagList?.add(it.cosplayName!!)
            tagList?.containsAll(_tagList.value ?: listOf()) ?: false
        }?.filter { it ->

            val avg = it.avgReviews?.replace(',', '.')?.toFloat() ?: 0.0f

            avg <= maxVal.toFloat() && avg >= minVal.toFloat() &&
                    if (it.time != null && timeToHours(_cosplayFormTime.value!!) != 0L) {
                        timeToHours(it.time!!) <= timeToHours(_cosplayFormTime.value!!)
                    } else  {
                        true
                    }
        }

        if (asc) {
            _cosplayListFiltered.value = firstFilter?.sortedBy { it.avgReviews?.replace(',', '.')?.toFloat() ?: 0.0f }
        }
        else {
            _cosplayListFiltered.value = firstFilter?.sortedByDescending { it.avgReviews?.replace(',', '.')?.toFloat() ?: 0.0f }
        }
    }

    private fun timeToHours(time: MutableMap<String, Int>): Long {
        var hours = 0L
        for (a in time) {
            when (a.key) {
                "anni" -> hours += a.value * 365 * 24
                "mesi" -> hours += a.value * 30 * 24
                "settimane" -> hours += a.value * 7 * 24
                "giorni" -> hours += a.value * 24
                "ore" -> hours += a.value
            }
        }

        return hours
    }

    fun setStepVideo(step: TutorialStep?) {
        _stepVideo.value = step
    }

    fun setTodoStepVideo(step: ToDoStep?) {
        _todoStepVideo.value = step
    }

    fun cosplayEditSet() {
        _formImages.value = _zoomCard.value?.imgUrls
        _formTitle.value = _zoomCard.value?.cosplayName
        _formDescription.value = _zoomCard.value?.description
        _cosplayFormTags.value = _zoomCard.value?.tags
        _cosplayFormMaterialDescription.value = _zoomCard.value?.material ?: ""

        _cosplayFormTime.value = mutableMapOf<String, Int>("anni" to 0, "mesi" to 0, "settimane" to 0, "giorni" to 0, "ore" to 0)
        if (_zoomCard.value?.time != null) {
            for (a in _zoomCard.value!!.time!!.filter { it.value > 0 }) {
                _cosplayFormTime.value!![a.key] = a.value
            }
        }

        _cosplayFormTutorial.value = _zoomCard.value?.tutorial

        _currentStep.value = 1

        if (_zoomCard.value?.tutorial != null)
            _totalSteps.value = _zoomCard.value?.tutorial!!.size + 7
        else
            _totalSteps.value = 7

        if (_zoomCard.value?.imgUrls?.isNotEmpty() == true) {
            _cosplayEditImages.value = mutableMapOf<String, String>()
            for (img in _zoomCard.value?.imgUrls!!)
                _cosplayEditImages.value!![img.key] = img.value
        }

        _cosplayEdit.value = _zoomCard.value

        _zoomCard.value = null
    }

    fun setStepForTodo(step: TutorialStep) {
        _stepForTodo.value = step
    }

    fun annulla(cosplaysImgsSRef: StorageReference, profileIconSR: StorageReference) {


        if (_todoEdit.value != null) {
            if (_formImages.value?.isNotEmpty() == true)
                cosplaysImgsSRef.child(_formImages.value!!.keys.first()).delete()

            _todoEdit.value = null
        }

        else if (_cosplayEdit.value != null) {
            if (_formImages.value?.isNotEmpty() == true) {
                for (img in _formImages.value!!) {
                    if (!(_cosplayEditImages.value!!.keys.contains(img.key))) {
                        cosplaysImgsSRef.child(img.key).delete()
                    }
                }
            }

            _cosplayEdit.value = null
            _cosplayEditImages.value = null
        }

        else if (_profileEdit.value != null) {
            profileIconSR.child(_username.value!!+"Edit").delete()
            _profileEdit.value = null
        }

        else if (_formImages.value?.isNotEmpty() == true) {
            for (img in _formImages.value!!) {
                cosplaysImgsSRef.child(img.key).delete()
            }
        }

        clearForm()
    }

    fun saveProfile(uDB: DatabaseReference, profileIconSR: StorageReference) {
        uDB.child(_username.value!!).child("emailAddress").setValue(_cosplayFormMaterialDescription.value)
        uDB.child(_username.value!!).child("bio").setValue(_formDescription.value)
        uDB.child(_username.value!!).child("username").setValue(_formTitle.value)
        uDB.child(_username.value!!).child("phoneNumber").setValue(_currentTag.value)

        if (_currentUser.value?.profileImgUrl != _profileEdit.value!!.profileImgUrl) {
            profileIconSR.child(_username.value!!).putFile(_profileEditImg.value!!)
            profileIconSR.child(_username.value!!).downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                uDB.child(_username.value!!).child("profileImgUrl").setValue(downloadUrl)
            }
            profileIconSR.child(_username.value!!+"Edit").delete()
        }

        clearForm()
    }

    fun getCheck(todoTitle: String, step: String) : Boolean {
        return _yourTodo.value?.get(todoTitle)?.steps?.get(step)?.completed ?: false
    }

    fun getCompletedNumber(todoTitle: String) : String {
        return _yourTodo.value?.get(todoTitle)?.steps?.filter { it.value.completed ?: false }?.size.toString() ?: "0"
    }

    fun setEliminate(bool: Boolean) {
        _eliminate.value = bool
    }

    fun notImplemented(context: Context) {
        Toast.makeText(context, "Funzione non implementata", Toast.LENGTH_LONG).show()
    }

    fun textLimit(context: Context) {
        Toast.makeText(context, "Limite di caratteri raggiunto", Toast.LENGTH_SHORT).show()
    }
}

enum class PopupType() {
    CARD,
    FILTER,
    IMPOSTAZIONI,
    ERROR,
    WARNING,
    NONE
}

enum class WarningType() {
    SALVACOSPLAY,
    SALVATODO,
    ELIMINACOSPLAY,
    MODIFICACOSPLAY,
    ELIMINATODO,
    TODODACOSPLAY,
    ELIMINATODOSTEP,
    ELIMINACOSLAYSTEP,
    ANNULLA,
    COSPLAYSTEPMANCANTI,
    TODOSTEPMANCANTI,
    MODIFICAPROFILO,
    NONE
}

enum class TimeType() {
    anni,
    mesi,
    settimane,
    giorni,
    ore
}