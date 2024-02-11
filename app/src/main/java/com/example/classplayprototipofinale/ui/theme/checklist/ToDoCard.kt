package com.example.classplayprototipofinale.ui.theme.checklist

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.PopupType
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.WarningType
import com.example.classplayprototipofinale.models.ToDo
import com.example.classplayprototipofinale.models.ToDoStep
import com.example.classplayprototipofinale.navigation.Screen
import com.example.classplayprototipofinale.ui.theme.BlueGradientCol
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.MyTypography
import com.example.classplayprototipofinale.ui.theme.RedCol
import com.example.classplayprototipofinale.ui.theme.ToDoCol
import com.google.firebase.database.DatabaseReference

class ToDoCard {
    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun ChecklistCard(todoTitle: String, cpvm: ClassPlayViewModel, uDB: DatabaseReference, ma: MainActivity, todo: ToDo, navController: NavController, done: Boolean, doneTodos: List<ToDo>
    ) {

        var todoSteps by remember { mutableStateOf(todo.steps!!.values) }

        cpvm.currentUser.observe(ma) {
            if (it.yourToDo != null) {
                if (it.yourToDo!![todoTitle]?.steps?.values != null) {
                    todoSteps = it.yourToDo!![todoTitle]?.steps!!.values
                }
            }
        }

        val painter = rememberImagePainter(data = todo.img!!.values.first())

        if (done && doneTodos.contains(todo) || !done && !doneTodos.contains(todo)) {
            Column (modifier = Modifier
                .width(330.dp)
                .heightIn(220.dp)
                .shadow(10.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            BlueGradientCol,
                            BottomBarCol
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    ), RoundedCornerShape(6)
                )
                .padding(10.dp)
                .padding(start = 5.dp)){
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Text(text = todo.todoTitle!!, fontSize = 25.sp, style = MyTypography.typography.body2)
                    Text(text = cpvm.getCompletedNumber(todoTitle) + "/" + todoSteps.size.toString(), style = MyTypography.typography.body1, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row (modifier = Modifier.fillMaxWidth()){
                    Column (modifier = Modifier.width(85.dp)){
                        Image(painter = painter, contentDescription = "Immagine Todo", modifier = Modifier
                            .size(80.dp, 100.dp)
                            .clip(RoundedCornerShape(10)), contentScale = ContentScale.Crop)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row {
                            Column(modifier = Modifier
                                .size(30.dp)
                                .shadow(5.dp, CircleShape)
                                .background(ToDoCol, CircleShape), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Icon(painter = painterResource(id = R.drawable.pencil), contentDescription = "Icona dello step", tint = Color.White, modifier = Modifier
                                    .clickable {
                                        cpvm.setTodoEdit(getCurrentTodo(cpvm, ma, todo.todoTitle!!))
                                        navController.navigate(Screen.NewTodo.route) {
                                            navController.graph.startDestinationRoute?.let { route ->
                                                popUpTo(route) {
                                                    saveState = true
                                                }
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                    .size(22.dp))
                            }

                            Spacer(modifier = Modifier.width(20.dp))

                            Column(modifier = Modifier
                                .size(30.dp)
                                .shadow(5.dp, CircleShape)
                                .background(RedCol, CircleShape)
                                .clickable {
                                    cpvm.setDestination(null)
                                    cpvm.setCardPopup(
                                        PopupType.WARNING,
                                        "Vuoi eliminare questa ToDo list?\n\nUna volta eliminata non sarà pù recuperabile!",
                                        WarningType.ELIMINATODO
                                    )
                                    cpvm.setTodoEdit(todo)
                                }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Icon(painter = painterResource(id = R.drawable.bin), contentDescription = "Icona dello step", tint = Color.White, modifier = Modifier.size(30.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column (modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, end = 5.dp)){

                        val size = todoSteps.size
                        var openAll by remember { mutableStateOf(false) }

                        val todoOrdered = todoSteps.sortedBy { it.step }


                        for (step in todoOrdered.take(3 + (size) * boolToInt(openAll))) {

                            val painter = rememberImagePainter(data = step.icon)

                            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                                Column {
                                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                                        Column(modifier = Modifier
                                            .size(25.dp)
                                            .shadow(5.dp, CircleShape)
                                            .background(ToDoCol, CircleShape), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                            Icon(painter = painter, contentDescription = "Icona dello step", tint = Color.White, modifier = Modifier.size(20.dp))
                                        }

                                        Text(text = step.title!!, fontSize = 18.sp, style = MyTypography.typography.body1, modifier = Modifier.width(120.dp))

                                        var check by remember { mutableStateOf(cpvm.getCheck(todoTitle, "s${step.step?.minus(1)}")) }

                                        Box(modifier = Modifier
                                            .size(25.dp)
                                            .border(2.dp, Color.White, CircleShape)
                                            .clickable {
                                                checkStep(uDB, step, todo, cpvm, check)
                                                check = !check
                                            }) {
                                            if (check)
                                                Image(painter = painterResource(id = R.drawable.check), contentDescription = "Fatto", modifier = Modifier.fillMaxSize())
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        if (size > 3){
                            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                                if (openAll)
                                    Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Mostra meno", modifier = Modifier
                                        .clickable {
                                            openAll = false
                                        }
                                        .rotate(90f), tint = Color.Gray)
                                else
                                    Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Mostra più", modifier = Modifier
                                        .clickable {
                                            openAll = true
                                        }
                                        .rotate(-90f), tint = Color.Gray)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    private fun checkStep(uDB: DatabaseReference, step: ToDoStep, todo: ToDo, cpvm: ClassPlayViewModel, check: Boolean) {
        val userUpdate = cpvm.currentUser.value

        userUpdate!!.yourToDo!![todo.todoTitle]!!.steps!!["s" + (step.step!! - 1).toString()]!!.completed =
            !check

        uDB.child(cpvm.username.value!!).setValue(userUpdate)
    }

    private fun getCurrentTodo(cpvm: ClassPlayViewModel, ma: MainActivity, todoTitle: String): ToDo {
        var todo : ToDo? = ToDo()
        cpvm.currentUser.observe(ma) {
            if (it?.yourToDo?.get(todoTitle)!= null) {
                todo = it.yourToDo?.get(todoTitle)
            }
        }

        if (todo == null)
            return ToDo()

        return todo!!
    }

    private fun boolToInt(bool: Boolean) : Int {
        return if (bool)
            1
        else
            0
    }
}