package com.horizon.firebaseapp.views.servicesFirebase.CloudData

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.horizon.firebaseapp.components.BodySmall
import com.horizon.firebaseapp.components.TopAppBarViewFB
import com.horizon.firebaseapp.viewmodel.NotesViewModel

@Composable
fun AdNotesView(navController: NavController, notesVM: NotesViewModel) {

    var titleNote by remember { mutableStateOf("") }
    var bodyNote by remember { mutableStateOf("") }
    val ctx = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBarViewFB(
                "Note",
                navController
            ) {
                IconButton(
                    onClick = { notesVM.saveNote(titleNote, bodyNote) {
                        Toast.makeText(ctx, "Note Saved", Toast.LENGTH_SHORT).show()
                        navController.popBackStack() }
                    }
                ) {
                    Icon(
                        Icons.Default.Save,
                        "Save Note",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.CenterVertically)
        ) {
            OutlinedTextField(
                titleNote,
                { titleNote = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                bodyNote,
                { bodyNote = it },
                label = { Text("Note") },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }

}