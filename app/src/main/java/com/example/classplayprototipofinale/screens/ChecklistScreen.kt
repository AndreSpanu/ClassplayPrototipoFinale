package com.example.classplayprototipofinale.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.google.firebase.database.DatabaseReference
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.models.ToDo
import com.example.classplayprototipofinale.models.Users
import com.example.classplayprototipofinale.navigation.Screen
import com.example.classplayprototipofinale.ui.theme.checklist.ToDoCard


@Composable
fun CheckListScreen(navController: NavController, cpvm: ClassPlayViewModel, ma: MainActivity, uDB: DatabaseReference, user: Users) {

    val tc = ToDoCard()

    var done by remember { mutableStateOf(false) }
    var todos by remember { mutableStateOf(listOf<ToDo>()) }
    var doneTodos by remember { mutableStateOf(listOf<ToDo>()) }
    val todoMap = user.yourToDo

    cpvm.done.observe(ma) {
        done = it
        if (todoMap != null) {
            doneTodos = todoMap.values.filter { map -> map.steps!!.values.filter { step -> step.completed == false }.isEmpty() }
        }
    }

    cpvm.yourTodo.observe(ma) {
        todos = if (it?.values != null) {
            it.values.toList()
        } else
            mutableListOf()
    }



    Box (Modifier.fillMaxSize()) {
        Column (modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally){

            if (todos.isNotEmpty()) {
                for (todo in todos) {
                    tc.ChecklistCard(todo.todoTitle ?: "", cpvm, uDB, ma, todo, navController, done, doneTodos)
                }
            }
        }


        Column (modifier = Modifier
            .fillMaxSize()
            .padding(30.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End){
            Image(painter = painterResource(id = R.drawable.plus_white), contentDescription = "Crea una nuova todo list", modifier = Modifier
                .size(65.dp)
                .clickable {
                    cpvm.setTotalSteps(2)
                    cpvm.setTodoEdit(null)
                    cpvm.clearForm()

                    navController.navigate(Screen.NewTodo.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}