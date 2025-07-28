package com.horizon.firebaseapp.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun LogButtons(
    modifier: Modifier = Modifier,
    icon: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(icon),
            "",
            tint = Color.Unspecified,
            modifier = Modifier.size(35.dp)
        )
    }
}