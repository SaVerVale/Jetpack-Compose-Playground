package com.steleot.jetpackcompose.playground.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.steleot.jetpackcompose.playground.UiNavRoutes
import com.steleot.jetpackcompose.playground.compose.reusable.DefaultScaffold
import java.util.Locale

private const val Url = "ui/LocalLayoutDirectionScreen.kt"

@Composable
fun LocalLayoutDirectionScreen() {
    DefaultScaffold(
        title = UiNavRoutes.LocalLayoutDirection.capitalize(Locale.getDefault()),
        link = Url,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LocalLayoutDirectionScreenExample()
        }
    }
}

@Composable
private fun LocalLayoutDirectionScreenExample() {
    val layoutDirection = LocalLayoutDirection.current
    Text(text = "Local layout Direction is: $layoutDirection")
}