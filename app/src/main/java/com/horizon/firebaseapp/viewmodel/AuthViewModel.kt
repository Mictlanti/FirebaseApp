package com.horizon.firebaseapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizon.firebaseapp.model.ErrorMsg
import com.horizon.firebaseapp.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

private const val USERS = "Users"

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val fb = FirebaseFirestore.getInstance()

    private val _user = MutableStateFlow(UserModel())
    val userState: StateFlow<UserModel> = _user.asStateFlow()

    var error by mutableStateOf(ErrorMsg())
        private set

    //That suspend fun save a user in the database ans return a boolean
    suspend fun saveUser(userName: String) : Boolean {

        //if the session is not initialized, we return. In other words, if the user is not logged in, we return
        val uid = auth.currentUser?.uid ?: return false

        //Create a new model user with the uid an the username
        val user = UserModel(
            userId = uid,
            userName = userName
        )

        //We try to save the user in the database in the collection "Users"
        return try {
            Firebase.firestore.collection(USERS)  //We get the collection "Users"
                .document(uid)  //We select the document with the uid
                .set(user)  //Save the user
                .await()  //Wait for the response
            //If all its ok, we return true
                Log.d("saveUser", "Usuario guardado")
                    true

        } catch (e : Exception) {
            //If there is an error, we return false
            Log.e("saveUser", "Error: ${e.localizedMessage}")
            false
        }

    }

    //That fun get the user from the database
    fun getUser() {
        //if the session is not initialized, we return
        val uid = auth.currentUser?.uid ?: return

        try {
            //We try to get the user from the database and located the doc with the uid
            fb.collection(USERS)
                .document(uid)
                .get()  //We get the document
                .addOnSuccessListener { doc ->
                    //Convert the document to a UserModel type object
                    val user = doc.toObject(UserModel::class.java)
                    Log.d("getUser", "User: $user")

                    //We update the user and the error
                    _user.value = user ?: UserModel()
                    error = ErrorMsg()
                }
                .addOnFailureListener {
                    //If there is an error, we update the error
                    Log.e("getUser", "Error: ${it.localizedMessage}")
                    error = ErrorMsg(true)
                }
        } catch (e: Exception) {
            Log.e("getUser", "Error: ${e.localizedMessage}")
            error = ErrorMsg(true)
        }

    }

}