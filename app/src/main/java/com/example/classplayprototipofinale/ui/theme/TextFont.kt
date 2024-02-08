package com.example.classplayprototipofinale.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit


class TextFont {
    @Composable
    fun OutlinedText(text: String, underline: Boolean, fontSize: TextUnit) {
        Box {
            Text(text = text, style = MyTypography.typography.h3, fontSize = fontSize)
            if (underline)
                Text(text = text, style = MyTypography.typography.h2, textDecoration = TextDecoration.Underline, fontSize = fontSize)
            else
                Text(text = text, style = MyTypography.typography.h2, fontSize = fontSize)
        }
    }
}