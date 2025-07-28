package com.horizon.firebaseapp.views.servicesFirebase.CloudData

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.horizon.firebaseapp.components.BodyLarge
import com.horizon.firebaseapp.components.TopAppBarViewFB
import com.horizon.firebaseapp.viewmodel.NotesViewModel

@Composable
fun EditNoteRoute(
    navController: NavController,
    notesVM: NotesViewModel,
    idDoc: String
) {

    val state = notesVM.state

    LaunchedEffect(Unit) {
        notesVM.getNoteById(idDoc)
    }

    Scaffold(
        topBar = {
            TopAppBarViewFB(
                "Note",
                navController
            ) {
                IconButton(
                    onClick = {
                        notesVM.deleteNote(idDoc) {
                            navController.popBackStack()
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        "Delete Note",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(
                    onClick = {
                        notesVM.editNote(idDoc) {
                            navController.popBackStack()
                            Toast.makeText(navController.context, "Note Updated", Toast.LENGTH_SHORT).show()
                        }
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
            verticalArrangement = Arrangement.spacedBy(
                20.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            OutlinedTextField(
                state.title,
                { notesVM.onValueChange(it, "title") },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                state.body,
                { notesVM.onValueChange(it, "body") },
                label = { Text("Note") },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}