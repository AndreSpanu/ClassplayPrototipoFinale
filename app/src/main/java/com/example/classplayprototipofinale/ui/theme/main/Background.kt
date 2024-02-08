package com.example.classplayprototipofinale.ui.theme.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.ui.theme.BackgroundCol


class Background {
    @Composable
    fun SetBackground() {
        Box (modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCol)){

        }
    }
}