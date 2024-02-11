package com.example.classplayprototipofinale.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.classplayprototipofinale.R

object MyTypography {
    private val cpFontFamily = FontFamily(
        //Font(R.font.rubik_one_regular, FontWeight(700)),
        Font(R.font.montserrat_regular, FontWeight(300)),
        Font(R.font.montserrat_bold, FontWeight(500))
    )

    val typography = androidx.compose.material.Typography(
        /*h2 = TextStyle(
            fontFamily = rubikOneFontFamily,
            fontWeight = FontWeight(700),
            fontSize = 26.sp,
            color = Color.Black,
            drawStyle = Stroke(
                miter = 30f,
                width = 5f,
                join = StrokeJoin.Round
            )
        ),
        h3 = TextStyle(
            fontFamily = rubikOneFontFamily,
            fontWeight = FontWeight(700),
            fontSize = 26.sp,
            color = Color.White
        ),*/
        body1 = TextStyle(
            fontFamily = cpFontFamily,
            fontWeight = FontWeight(300),
            fontSize = 26.sp,
            color = Color.White
        ),
        body2 = TextStyle(
            fontFamily = cpFontFamily,
            fontWeight = FontWeight(500),
            fontSize = 26.sp,
            color = Color.White
        ),
    )
}


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)