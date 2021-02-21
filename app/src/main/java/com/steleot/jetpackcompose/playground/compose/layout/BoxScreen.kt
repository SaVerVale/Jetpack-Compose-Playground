package com.steleot.jetpackcompose.playground.compose.layout

import androidx.compose.runtime.Composable
import com.steleot.jetpackcompose.playground.FoundationLayoutNavRoutes
import com.steleot.jetpackcompose.playground.compose.reusable.DefaultScaffold
import java.util.*

@Composable
fun BoxScreen() {
    DefaultScaffold(
        title = FoundationLayoutNavRoutes.Box.capitalize(Locale.getDefault())
    ) {

    }
}