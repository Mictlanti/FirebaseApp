package com.horizon.firebaseapp.components.componentsServiesFB

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.horizon.firebaseapp.components.BodySmall

@Composable
fun MessageStyle(message: String) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()
            .background(
                MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        BodySmall(text = message)
    }
}