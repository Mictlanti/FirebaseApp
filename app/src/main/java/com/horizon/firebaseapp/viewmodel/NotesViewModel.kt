package com.horizon.firebaseapp.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizon.firebaseapp.model.NoteState
import com.horizon.firebaseapp.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val NOTES = "notes"
private const val TITLE = "title"
private const val BODY_NOTE = "body"
private const val DATE = "date"
private const val EMAIL_USER = "emailUser"

class NotesViewModel : ViewModel() {

    private val fb = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    private val _notes = MutableStateFlow<List<NoteState>>(emptyList())
    val notes: StateFlow<List<NoteState>> = _notes

    var state by mutableStateOf(NoteState())
        private set

    fun onValueChange(value: String, text: String) {
        when(text) {
            "title" -> state = state.copy(title = value)
            "body" -> state = state.copy(body = value)
        }
    }

    //We save a note in the database
    fun saveNote(title: String, note: String, onSuccess: () -> Unit) {

        //We get the email of the user
        val email = auth.currentUser?.email

        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Create a map with the note data
                val newNote = hashMapOf(
                    TITLE to title,  //Title of the note
                    BODY_NOTE to note,  //Body of the note
                    DATE to formatDate(),  //Date of the note
                    EMAIL_USER to email  //Email of the user
                )

                //We add the note to the database in the collection "notes"
                fb.collection(NOTES).add(newNote)
                    .addOnSuccessListener {
                        //If the note is saved, we call the onSuccess function
                        onSuccess()
                    }
            } catch (e: Exception) {
                //Else we log the error
                Log.d("NotesViewModel", "Error saving note: ${e.localizedMessage}")
            }
        }
    }

    //This function edit a note in the database
    fun editNote(idDoc: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //We create a map with the new data of the note
                val editNote = hashMapOf(
                    TITLE to state.title, //New title of the note from the current state
                    BODY_NOTE to state.body //New body of the note from the current state
                )

                //We get the note from the database with the id of the note, later update it with the new data
                fb.collection(NOTES).document(idDoc)
                    .update(editNote as Map<String, Any>) //We update the note with the new data
                    .addOnSuccessListener {
                        //If the note is updated, we call the onSuccess function
                        onSuccess()
                    }
            } catch (e: Exception) {
                Log.d("NotesViewModel", "Error to edit note: ${e.localizedMessage}")
            }
        }
    }

    fun deleteNote(idDoc: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                fb.collection(NOTES).document(idDoc)
                    .delete()
                    .addOnSuccessListener {
                        onSuccess()
                    }
            } catch (e: Exception) {
                Log.d("NotesViewModel", "Error to delete note: ${e.localizedMessage}")
            }
        }
    }

    //We get all the notes from the database in the realtime
    fun getNotes() {

        //We get the email of the user, if the user is null, out of the function
        val email = auth.currentUser?.email ?: return

        //Access to the database and get all the notes from the collection "notes"
        fb.collection(NOTES)
            .whereEqualTo(EMAIL_USER, email) //Filter the notes by the email of the user
            .addSnapshotListener { querySnapshot, error ->
                //If there is an error, finish the function
                if (error != null) {
                    return@addSnapshotListener
                }
                //Create a list of notes temporally
                val doc = mutableListOf<NoteState>()

                //If the querySnapshot is not null, we get all the documents from the collection "notes"
                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        //We get the data of the document and we add it to the list of notes and we add the id of the document to the note
                        val myDoc =
                            document.toObject(NoteState::class.java).copy(idDoc = document.id)
                        doc.add(myDoc)
                    }
                }
                //Update the list of notes
                _notes.value = doc
            }
    }

    //Access the database and get the note by id
    fun getNoteById(docId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //We get the note from the database with the id of the note
                fb.collection(NOTES).document(docId)
                    .addSnapshotListener { snapshot, _->
                        //If the snapshot is not null, we get the data of the note and we update the current state
                        if (snapshot != null) {
                            val note = snapshot.toObject(NoteState::class.java)
                            state = state.copy(
                                title = note?.title ?: "",
                                body = note?.body ?: ""
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.d("NotesViewModel", "Error getting note by ID: ${e.localizedMessage}")
            }
        }
    }

    private fun formatDate(): String {
        //We get the current date
        val currentDate = Calendar.getInstance().time
        //We format the date to day/Month/year
        val res = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return res.format(currentDate)
    }

}