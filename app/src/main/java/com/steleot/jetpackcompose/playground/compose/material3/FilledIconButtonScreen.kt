package com.steleot.jetpackcompose.playground.compose.material3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Panorama
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.steleot.jetpackcompose.playground.navigation.graph.Material3NavRoutes
import com.steleot.jetpackcompose.playground.ui.base.material3.DefaultScaffold

private const val Url = "material3/FilledIconButtonScreen.kt"

@Composable
fun FilledIconButtonScreen() {
    DefaultScaffold(
        title = Material3NavRoutes.FilledIconButton,
        link = Url,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefaultFilledIconButton()
            DisabledFilledIconButton()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DefaultFilledIconButton() {
    FilledIconButton(
        onClick = {},
        content = {
            Icon(
                Icons.Filled.Palette,
                modifier = Modifier.size(48.dp),
                contentDescription = null,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisabledFilledIconButton() {
    FilledIconButton(
        onClick = {},
        content = {
            Icon(
                Icons.Filled.Panorama,
                contentDescription = null
            )
        },
        enabled = false
    )
}