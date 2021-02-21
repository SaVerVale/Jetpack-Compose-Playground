package com.steleot.jetpackcompose.playground.compose.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.steleot.jetpackcompose.playground.FoundationLayoutNavRoutes
import com.steleot.jetpackcompose.playground.compose.reusable.DefaultScaffold
import java.util.*

@Composable
fun SpacerScreen() {
    DefaultScaffold(
        title = FoundationLayoutNavRoutes.Spacer.capitalize(Locale.getDefault())
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp)
        ) {
            Text(
                text = "There"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "is"
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "spacer"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(
                    text = "between"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "even"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "in"
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    text = "row"
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}