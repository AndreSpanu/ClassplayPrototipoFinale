package com.example.classplayprototipofinale.ui.theme.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.MainActivity
import com.example.classplayprototipofinale.ui.theme.TagCol
import kotlin.math.roundToInt

class TagsList {

    /** Lo slider di tag nella HomePage **/

    @Composable
    fun TagSlider(tags: Set<String>, cpvm: ClassPlayViewModel) {

        var scrollState = rememberScrollState()
        var maxScroll by remember { mutableStateOf(0) }
        var scrollOffset by remember { mutableStateOf(0f) }
        var rowWidth by remember { mutableStateOf(0) }
        var width by remember { mutableStateOf(0f) }

        Row (
            Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp, 20.dp, 0.dp)
                .horizontalScroll(scrollState)){
            for (tag in tags) {
                Box(modifier = Modifier
                    .background(TagCol, RoundedCornerShape(50))
                    .padding(10.dp, 3.dp, 5.dp, 3.dp)
                ) {
                    Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = tag, fontSize = 16.sp, color = Color.White)

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(imageVector = Icons.Default.Clear,
                            contentDescription = "Remove Tag",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    cpvm.removeSearchingTag(tag)
                                },
                            tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(10.dp))

        maxScroll = calculateScrollRange(scrollState)

        if (maxScroll != 0) {

            width = 1/((maxScroll.toFloat() + rowWidth)/rowWidth)
            val a = rowWidth - width * rowWidth
            val b = scrollState.value.toFloat() / maxScroll
            scrollOffset = a * b

            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .height(4.dp)
                    .background(Color.White, RoundedCornerShape(50))
                    .onGloballyPositioned {layoutCoordinates ->
                        rowWidth = layoutCoordinates.size.width
                    }) {
                Box(modifier = Modifier
                    .fillMaxWidth(width)
                    .fillMaxHeight()
                    .absoluteOffset(scrollOffset.dp)
                    .background(TagCol, RoundedCornerShape(50)))
            }
        }
    }

    /** Lo slider di tag presente nel form **/

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun TagSliderForm(cpvm: ClassPlayViewModel, ma: MainActivity) {

        var tags by remember { mutableStateOf(mutableListOf<String>()) }

        cpvm.cosplayFormTags.observe(ma) { it -> tags = it }

        FlowRow (
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)){
            for (tag in tags) {
                Box(modifier = Modifier
                    .padding(bottom = 3.dp)
                    .background(TagCol, RoundedCornerShape(50))
                    .height(32.dp)
                    .padding(10.dp, 3.dp, 5.dp, 3.dp)
                ) {
                    Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = tag, fontSize = 16.sp, color = Color.White)

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(imageVector = Icons.Default.Clear,
                            contentDescription = "Remove Tag",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    cpvm.removeTagCosplayForm(tag)
                                },
                            tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }

    @Composable
    fun calculateScrollRange(scrollState: ScrollState): Int {
        val density = LocalDensity.current.density
        val maxScroll = with(LocalDensity) {
            (scrollState.maxValue * density).roundToInt()
        }
        return maxScroll
    }
}