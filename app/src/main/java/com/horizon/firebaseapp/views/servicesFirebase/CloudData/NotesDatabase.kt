package com.horizon.firebaseapp.views.servicesFirebase.CloudData

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.horizon.firebaseapp.components.BodyLarge
import com.horizon.firebaseapp.components.TopAppBarViewFB
import com.horizon.firebaseapp.components.componentsServiesFB.CardNote
import com.horizon.firebaseapp.navigation.AppScreens
import com.horizon.firebaseapp.viewmodel.AuthViewModel
import com.horizon.firebaseapp.viewmodel.NotesViewModel

@Composable
fun NotesDatabase(
    navController: NavController,
    noteVM: NotesViewModel
) {
    LaunchedEffect(Unit) {
        noteVM.getNotes()
    }

    Scaffold(
        topBar = {
            TopAppBarViewFB("Notes", navController) {
                IconButton(
                    onClick = {
                        navController.navigate(AppScreens.NotesScreen.route)
                    }
                ) {
                    Icon(
                        Icons.Default.Add,
                        "Add Note",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

            val notes by noteVM.notes.collectAsState()

            LazyColumn {
                itemsIndexed(notes) { index, note ->
                    CardNote(
                        title = note.title,
                        body = note.body,
                        date = note.date
                    ) {
                        navController.navigate(AppScreens.EditNoteScreen.route + "/${note.idDoc}")
                    }
                }
            }
        }
    }
}

