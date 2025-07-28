package com.horizon.firebaseapp.components.componentsServiesFB

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.DragStartHelper
import com.horizon.firebaseapp.components.BodyLarge
import com.horizon.firebaseapp.components.BodySmall

@Composable
fun CardNote(
    title: String,
    body: String,
    date: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    var showNotes by remember { mutableStateOf(false) }

    ShowAlert(
        showNotes,
        title,
        body,
        onDismiss = { showNotes = false }
    )

    Column(
        modifier = Modifier
            .padding(20.dp)
            .clickable { showNotes = true }
    ) {
        Row {
            Column {
                BodyLarge(text = title)
                BodySmall(text = date, color = Color.Gray)
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = { onClick() }
            ) {
                Icon(
                    Icons.Default.Edit,
                    "Edit Note"
                )
            }
        }
        HorizontalDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAlert(
    showNotes: Boolean,
    title: String,
    body: String,
    onDismiss: () -> Unit
) {
    if (showNotes){
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            dragHandle = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.CenterVertically),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    BodyLarge(text = title)
                    BottomSheetDefaults.DragHandle()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 50.dp, top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BodySmall(text = body)
            }
        }
    }
}