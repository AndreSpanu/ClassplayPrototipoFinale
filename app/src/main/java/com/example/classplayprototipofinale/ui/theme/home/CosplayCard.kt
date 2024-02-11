package com.example.classplayprototipofinale.ui.theme.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.PopupType
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.WarningType
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.navigation.Screen
import com.example.classplayprototipofinale.ui.theme.BlueGradientCol
import com.example.classplayprototipofinale.ui.theme.BubbleCol
import com.example.classplayprototipofinale.ui.theme.BottomBarCol
import com.example.classplayprototipofinale.ui.theme.DetailsCol
import com.example.classplayprototipofinale.ui.theme.MyTypography
import com.example.classplayprototipofinale.ui.theme.TagCol
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CosplayCard {

    /** La card nella miniatura della HomePage **/

    @Composable
    fun HomeCard(cpvm: ClassPlayViewModel, cosplay: Cosplay, cpDB: DatabaseReference) {

        var avgReview by remember { mutableStateOf<String?>("0") }

        cpDB.child(cosplay.cosplayName!!).addValueEventListener(object :  ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    avgReview = snapshot.child("avgReviews").value as String?
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        val painter = rememberImagePainter(data = cosplay.imgUrls?.values?.first())

        Box(modifier = Modifier
            .padding(bottom = 15.dp)
            .size(160.dp, 220.dp)) {
            Image(painter = painter,
                contentDescription = cosplay.cosplayName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(13.dp))
                    .clickable {
                        cpvm.setZoomCard(cosplay)
                    },
                contentScale = ContentScale.Crop
            )
            Column (Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween){
                Row (
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, end = 5.dp), horizontalArrangement = Arrangement.End){
                    Row (
                        Modifier
                            .background(BubbleCol, RoundedCornerShape(50))
                            .padding(horizontal = 5.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                        Text(text = avgReview.toString(), style = MyTypography.typography.body1, fontSize = 3.5.em)
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(painter = painterResource(id = R.drawable.star_1), contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                }
                Row (
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp), horizontalArrangement = Arrangement.Center){
                    Row (
                        Modifier
                            .background(BubbleCol, RoundedCornerShape(50))
                            .padding(horizontal = 5.dp, vertical = 3.dp)){
                        Text(text = cosplay.cosplayName, style = MyTypography.typography.body1, fontSize = 3.5.em)
                    }
                }
            }
        }
    }


    /** La card zoomata **/

    @SuppressLint("MutableCollectionMutableState")
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun ZoomCard(cosplay: Cosplay, cpvm: ClassPlayViewModel, cpDB: DatabaseReference, uDB: DatabaseReference) {

        var columnScroll by remember { mutableIntStateOf(0) }
        var maxScroll by remember { mutableIntStateOf(0) }
        var scrollOffset by remember { mutableFloatStateOf(0f) }
        var columnHeight by remember { mutableIntStateOf(0) }
        var height by remember { mutableFloatStateOf(0f) }
        var favorites by remember { mutableStateOf<MutableList<String>?>(mutableListOf("")) }

        var b by remember { mutableFloatStateOf(0f) }

        var cosReview by remember { mutableStateOf<Long?>(null) }
        var avgReview by remember { mutableStateOf<String?>("0") }

        val imageReduce = if (maxScroll != 0)
            minOf( columnScroll / (maxScroll.toFloat() / 5), 1f )
        else
            0f

        val density = LocalDensity.current.density

        maxScroll = columnHeight

        val cosplayEventListener = cpDB.child(cosplay.cosplayName!!).addValueEventListener(object :  ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    cosReview = snapshot.child("reviews").child(cpvm.username.value!!).child("vote").value as Long?
                    avgReview = snapshot.child("avgReviews").value as String?
                    favorites = snapshot.child("favorites").value as MutableList<String>?
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        Box(modifier = Modifier
            .size(350.dp, 600.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BlueGradientCol,
                        BottomBarCol
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                ), RoundedCornerShape(6)
            )) {

            /****************************************************** La colonna che contiene le informazioni sul cosplay **********************************************************/

            Column (
                Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .padding(top = (220).dp, bottom = 20.dp)
                    .verticalScroll(
                        ScrollState(
                            if (columnScroll < maxScroll / 5)
                                0
                            else
                                columnScroll - maxScroll / 5
                        )
                    )
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if ((columnScroll - dragAmount.y).roundToInt() >= 0 && (columnScroll - dragAmount.y).roundToInt() <= maxScroll - ((320 + 260 * imageReduce).dp * density).value * 2)
                                    columnScroll -= dragAmount.y.roundToInt()

                                b =
                                    columnScroll / (maxScroll - ((320 + 260 * imageReduce).dp * density).value * 2)
                            }
                        )
                    }
                    .onGloballyPositioned { layoutCoordinates ->
                        columnHeight = layoutCoordinates.size.height
                    }){

                        CosplayMoreDetails(cosplay = cosplay, imageReduce = imageReduce, cpvm = cpvm)

            }

            /****************************************************** La riga che contiene le immagini e le informazioni primarie del cosplay **********************************************************/

            FlowRow (
                Modifier
                    .fillMaxWidth()
                    .padding(start = (imageReduce * 15).dp, top = (imageReduce * 40).dp)
                    ){

                if (cosplay.imgUrls?.isNotEmpty() == true)
                    CosplayImages(cosplay = cosplay, imageReduce = imageReduce)

                CosplayDetails(imageReduce = imageReduce, cosplay = cosplay, avgReview = avgReview, cosReview = cosReview, cpDB = cpDB, cpvm = cpvm, favorites = favorites, uDB)
            }

            /********************************************************************* Scrollbar ***************************************************************************/

            if (maxScroll != 0) {

                var scrollbarHeight by remember { mutableIntStateOf(0) }
                height = 0.2f
                val a = scrollbarHeight - height * scrollbarHeight
                scrollOffset = ((a * b) / density)

                Column (modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom){
                    Column (modifier = Modifier
                        .offset(x = 13.dp)
                        .fillMaxHeight()
                        .width(6.dp)
                        .background(Color.White, RoundedCornerShape(50))
                        .onGloballyPositioned { layoutCoordinates ->
                            scrollbarHeight = layoutCoordinates.size.height
                        }){
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(height)
                            .absoluteOffset(y = scrollOffset.dp)
                            .background(BubbleCol, RoundedCornerShape(50)))
                    }
                }
            }

            /*************************************************************** Close and Options ********************************************************************/
            CloseAndOptions(cpvm, cpDB, cosplayEventListener)
        }
    }

    @Composable
    fun TimeShow(cosplay: Cosplay) {
        val timeFiltered = cosplay.time?.filter { it.value > 0 }

        if (timeFiltered != null) {
            if (timeFiltered.isNotEmpty()) {
                val lastKey: String = timeFiltered.keys.last()

                Row (modifier = Modifier.padding(start = 15.dp)){
                    Text(text = "Tempo", style = MyTypography.typography.body2, fontSize = 25.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row (modifier = Modifier.padding(horizontal = 15.dp)){
                    val time = timeFiltered.filter { it.key != lastKey }.entries.fold("") { accumulator, entry ->
                        val (key, value) = entry
                        "$accumulator $value $key "
                    }

                    if (time != "")
                        Text(text = "$time e ${timeFiltered[lastKey]} $lastKey", style = MyTypography.typography.body1, fontSize = 15.sp)
                    else
                        Text(text = "${timeFiltered[lastKey]} $lastKey", style = MyTypography.typography.body1, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(15.dp))

            }
        }
    }

    @Composable
    fun CloseAndOptions(cpvm: ClassPlayViewModel, cpDB: DatabaseReference, cosplayEventListener: ValueEventListener) {
        Row (
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = "Close the card", modifier = Modifier
                .clickable {
                    cpvm.setCardPopup(PopupType.CARD)
                }
                .size(35.dp), tint = Color.White)

            Box(modifier = Modifier
                .size(30.dp)
                .background(Color.White, RoundedCornerShape(50))) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close the card", modifier = Modifier
                    .clickable {
                        cpDB.removeEventListener(cosplayEventListener)
                        cpvm.setZoomCard(null)
                        cpvm.setCardPopup(PopupType.NONE)
                    }
                    .fillMaxSize(), tint = BottomBarCol)
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun CosplayMoreDetails(cosplay: Cosplay, imageReduce: Float, cpvm: ClassPlayViewModel) {
        Column (modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 600.dp)){

            Spacer(modifier = Modifier.height((380 - 260 * imageReduce).dp))

            /** TagList **/

            FlowRow (modifier = Modifier.padding(horizontal = 15.dp)){
                Text(text = "Tag", style = MyTypography.typography.body2, fontSize = 25.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow (
                Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)){
                for (tag in cosplay.tags!!.toSet()) {
                    Text(text = tag, fontSize = 16.sp, style = MyTypography.typography.body1, modifier = Modifier
                        .padding(bottom = 5.dp)
                        .background(TagCol, RoundedCornerShape(50))
                        .padding(horizontal = 7.dp, vertical = 3.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            /** Description **/

            Row (modifier = Modifier.padding(start = 15.dp)){
                Text(text = "Descrizione", style = MyTypography.typography.body2, fontSize = 25.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row (modifier = Modifier.padding(horizontal = 15.dp)){
                Text(text = cosplay.description!!, style = MyTypography.typography.body1, fontSize = 15.sp, textAlign = TextAlign.Justify)
            }

            Spacer(modifier = Modifier.height(15.dp))

            /** Time **/

            TimeShow(cosplay = cosplay)

            /** Materiali **/

            if (cosplay.material != null) {
                Row (modifier = Modifier.padding(start = 15.dp)){
                    Text(text = "Materiali", style = MyTypography.typography.body2, fontSize = 25.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row (modifier = Modifier.padding(horizontal = 15.dp)){
                    Text(text = cosplay.material!!, style = MyTypography.typography.body1, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(15.dp))
            }

            /** Tutorial **/

            if (cosplay.tutorial != null) {
                Row (modifier = Modifier.padding(start = 15.dp)){
                    Text(text = "Tutorial", style = MyTypography.typography.body2, fontSize = 25.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                for (step in cosplay.tutorial!!) {

                    val painter = rememberImagePainter(data = step.value.icon)

                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .padding(end = 5.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                        Text(text = (step.key.split("s")[1].toInt() + 1).toString(), style = MyTypography.typography.body1, fontSize = 17.sp, modifier = Modifier.weight(1f))

                        Row (modifier = Modifier
                            .weight(8f)
                            .padding(horizontal = 5.dp)
                            .clickable {
                                cpvm.setStepVideo(step.value)
                            }
                            .shadow(5.dp)
                            .background(Color.White, RoundedCornerShape(50))
                            .padding(3.dp)
                            .padding(end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                            Column (modifier = Modifier
                                .size(30.dp)
                                .background(DetailsCol, CircleShape), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                                Icon(painter = painter, contentDescription = null, modifier = Modifier.size(25.dp), tint = Color.White)
                            }
                            Text(text = step.value.componentName!!, fontSize = 15.sp, style = MyTypography.typography.body1, color = Color.Black)
                        }

                        Icon(painter = painterResource(id = R.drawable.plus_image), contentDescription = "Crea una todo con questo step", modifier = Modifier
                            .size(38.dp)
                            .clickable {
                                cpvm.setStepForTodo(step.value)
                                cpvm.setCardPopup(
                                    PopupType.WARNING,
                                    "Creare una ToDo da questo step?",
                                    WarningType.TODODACOSPLAY
                                )
                                cpvm.setDestination(Screen.NewTodo.route)
                            }, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height((380 + 260 * imageReduce).dp))
        }
    }

    @Composable
    fun CosplayDetails(imageReduce: Float, cosplay: Cosplay, avgReview: String?, cosReview: Long?, cpDB: DatabaseReference, cpvm: ClassPlayViewModel, favorites: MutableList<String>?, uDB: DatabaseReference) {

        val sr = StarReview()

        Column (
            Modifier
                .width((350 - imageReduce * 160).dp)
                .height((180 - imageReduce * 60).dp), verticalArrangement = Arrangement.SpaceBetween){

            /** CosplayName **/

            Row (Modifier
                .padding(start = 15.dp)){
                cosplay.cosplayName?.let { Text(text = it, fontSize = (25 - imageReduce * 8).sp, style = MyTypography.typography.body2) }
            }

            /** Reviews **/

            Row (Modifier
                .padding(start = 15.dp), verticalAlignment = Alignment.CenterVertically){

                if (cosplay.username == cpvm.username.value) {
                    for (i in (1..5)) {
                        sr.AvgReviews(avgReview = avgReview!!.toDouble(), i = i, size = 30 - imageReduce * 10)
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                    Text(text = avgReview.toString(), fontSize = (25 - imageReduce * 10).sp, modifier = Modifier.padding(start = 5.dp), style = MyTypography.typography.body1)
                }
                else {
                    if (imageReduce < 0.5f) {
                        if (cosReview != null) {
                            for (i in (1..5)) {
                                sr.UpdateReview(i = i, size = 30 - imageReduce * 10, cosReview = cosReview)
                                Spacer(modifier = Modifier.width(2.dp))
                            }
                            Text(text = cosReview.toString(), fontSize = (25 - imageReduce * 10).sp, modifier = Modifier.padding(horizontal = 5.dp), style = MyTypography.typography.body1)
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Elimina recensione", tint = Color.White, modifier = Modifier.size(38.dp).clickable {
                                sr.saveAvarageDelete(cpDB, cpvm, cosplay)
                            })
                        }
                        else {
                            for (i in (1..5)) {
                                sr.NewReview(cpvm = cpvm, i = i, cpDB = cpDB, cosplay = cosplay, avgReview = avgReview, size = 30 - imageReduce * 10)
                                Spacer(modifier = Modifier.width(2.dp))
                            }
                        }
                    }

                    else {
                        for (i in (1..5)) {
                            sr.AvgReviews(avgReview = avgReview!!.toDouble(), i = i, size = 30 - imageReduce * 10)
                            Spacer(modifier = Modifier.width(2.dp))
                        }
                        Text(text = avgReview.toString(), fontSize = (20 - imageReduce * 5).sp, modifier = Modifier.padding(start = 5.dp), style = MyTypography.typography.body1)
                    }
                }
            }

            /** Username **/

            Row (
                Modifier
                    .padding(start = 15.dp)
                    .clickable {
                        cpvm.setOtherProfile(cosplay.username, uDB)
                    }){
                Text(text = "@", fontSize = 20.sp, style = MyTypography.typography.body1)
                Text(text = cosplay.username!!, fontSize = 20.sp, style = MyTypography.typography.body1, textDecoration = TextDecoration.Underline)
            }

            /** Date and Favorite **/

            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 30.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = cosplay.date!!, fontSize = 15.sp, style = MyTypography.typography.body1)

                if (cosplay.username != cpvm.username.value) {
                    if (favorites?.contains(cpvm.username.value) == true)
                        Image(painter = painterResource(id = R.drawable.heart), contentDescription = "Rimuovi dai preferiti", modifier = Modifier
                            .size((45 - imageReduce * 20).dp)
                            .clickable {
                                val newList =
                                    favorites.filter { it != cpvm.username.value }
                                cosplay.cosplayName?.let {
                                    cpDB
                                        .child(it)
                                        .child("favorites")
                                        .setValue(newList)
                                }
                            }, colorFilter = ColorFilter.tint(Color.Red))
                    else
                        Image(painter = painterResource(id = R.drawable.heart), contentDescription = "Aggiungi ai preferiti", modifier = Modifier
                            .size((45 - imageReduce * 20).dp)
                            .clickable {
                                if (favorites != null) {
                                    favorites.add(cpvm.username.value!!)
                                    cosplay.cosplayName?.let {
                                        cpDB
                                            .child(it)
                                            .child("favorites")
                                            .setValue(favorites)
                                    }
                                } else {
                                    val newList = listOf(cpvm.username.value)
                                    cosplay.cosplayName?.let {
                                        cpDB
                                            .child(it)
                                            .child("favorites")
                                            .setValue(newList)
                                    }
                                }
                            })
                }
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun CosplayImages(cosplay: Cosplay, imageReduce: Float) {

        val density = LocalDensity.current.density
        val stepDistance = (400.dp * density).value
        var currentIndex by remember { mutableIntStateOf(0) }
        var newIndex by remember { mutableIntStateOf(0) }
        var isAnimating by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val animatedScrollValue = remember { Animatable((currentIndex) * stepDistance) }
        val maxIndex = cosplay.imgUrls?.size

        val painterList : List<ImagePainter> =
            cosplay.imgUrls?.values?.map { rememberImagePainter(data = it) } ?: listOf()

        var dragEnd = 0f

        fun animateScroll() {
            if (!isAnimating) {
                isAnimating = true
                coroutineScope.launch {
                    currentIndex = newIndex
                    animatedScrollValue.animateTo((newIndex) * stepDistance, animationSpec = tween(500))
                    isAnimating = false
                }
            }
        }
        if (imageReduce < 0.02f) {

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)) {
                Row(modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(ScrollState(animatedScrollValue.value.toInt()))
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { dragEnd = 0f },
                            onDrag = { change, dragAmount ->

                                change.consume()

                                if (!isAnimating)
                                    dragEnd -= dragAmount.x
                            },
                            onDragEnd = {
                                if (dragEnd > 200f && !isAnimating && (currentIndex < maxIndex!! - 1)) {
                                    newIndex = currentIndex + 1
                                    animateScroll()
                                } else if (dragEnd < -200f && !isAnimating && currentIndex > 0) {
                                    newIndex = currentIndex - 1
                                    animateScroll()
                                }
                            }
                        )
                    }) {

                    for ( i in 0 until maxIndex!!) {
                        Image(
                            painter = painterList.elementAt(i),
                            contentDescription = "Immagine cosplay",
                            modifier = Modifier
                                .size(350.dp, 400.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = (20 - imageReduce * 10).dp,
                                        topEnd = (20 - imageReduce * 10).dp,
                                        bottomStart = (imageReduce * 10).dp,
                                        bottomEnd = (imageReduce * 10).dp
                                    )
                                ),
                            contentScale = ContentScale.Crop)
                    }
                }

                if (maxIndex!! > 1) {
                    Column (modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 5.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally){
                        Row {
                            for (i in 0 until maxIndex) {
                                if (i == currentIndex) {
                                    Icon(imageVector = Icons.Default.Circle, contentDescription = i.toString(), tint = Color.LightGray, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                                else {
                                    Icon(imageVector = Icons.Default.Circle, contentDescription = i.toString(), tint = Color.Gray, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                            }
                        }
                    }
                }

            }
        }
        else {
            Image(
                painter = painterList.elementAt(currentIndex),
                contentDescription = "Immagine cosplay",
                modifier = Modifier
                    .width((350 * (1 - 0.6 * imageReduce)).dp)
                    .height((400 * (1 - 0.6 * imageReduce)).dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = (20 - imageReduce * 10).dp,
                            topEnd = (20 - imageReduce * 10).dp,
                            bottomStart = (imageReduce * 10).dp,
                            bottomEnd = (imageReduce * 10).dp
                        )
                    ),
                contentScale = ContentScale.Crop)
        }
    }
}