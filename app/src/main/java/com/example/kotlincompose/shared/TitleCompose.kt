package com.example.kotlincompose.shared

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TitleCompose(
    title: String,
    textUnit: TextUnit? = null,
    fontWeight: FontWeight? = null,
    color: Color? = null
) {
    Text(
        title,
        fontSize = textUnit ?: 18.sp,
        fontWeight = fontWeight ?: FontWeight.W600,
        color = color ?: Color.Black
    )
}