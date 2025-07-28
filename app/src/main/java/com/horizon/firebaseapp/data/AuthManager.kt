package com.horizon.firebaseapp.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.horizon.firebaseapp.R
import com.horizon.firebaseapp.navigation.AppScreens
import kotlinx.coroutines.tasks.await

//Defining auth results: Success or Error
sealed class AuthRes<out T> {
    //Contains a generic "data" value
    data class Success<T>(val data: T) : AuthRes<T>()

    //Indicated errorMessage. If there is an error, we use <Nothing> because there is not value of success
    data class Error(val errorMessage: String) : AuthRes<Nothing>()
}

class AuthManager(context: Context) {

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val signInClient = Identity.getSignInClient(context)

    private suspend fun signInAnonymously(): AuthRes<FirebaseUser> {
        //SignIn in firebase anonymously
        return try {
            val result = auth.signInAnonymously().await()
            AuthRes.Success(result.user ?: throw Exception("Error sign in"))
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error sign in")
        }
    }

    suspend fun linkAnonymousAccountWithGoogleCredential(credential: AuthCredential): AuthRes<FirebaseUser> {
        return try {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
                ?.linkWithCredential(credential)
                ?.await()

            firebaseUser?.user?.let {
                AuthRes.Success(it)
            } ?: throw Exception("Linking failed")
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Failed to link anonymous account")
        }
    }

    fun linkAnonymousAccountWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    fun singOut(navController: NavController) {
        auth.signOut()
        signInClient.signOut()
        navController.navigate(AppScreens.LogInScreen.route)
    }

    //User identification
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    //We need two things: The ID token and email user
    //This things we can get throw googleSignInOptions:
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    //When the process finally, we call this function to process the result
    //Return the result in AuthRest (success, error), equal signInAnonymously
    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount> {
        return try {
            val account = task.getResult(ApiException::class.java)
            AuthRes.Success(account)
        } catch (e: ApiException) {
            AuthRes.Error(e.message ?: "Google SignIn failed")
        }
    }

    //Authenticate the user using a Google credential
    suspend fun signInWithGoogleCredential(credential: AuthCredential): AuthRes<FirebaseUser> {
        return try {
            //SignIn with Firebase using google credential
            //We use .await() to suspend result until get success or exception
            val firebaseUser = auth.signInWithCredential(credential).await()
            firebaseUser.user?.let {
                AuthRes.Success(it)
            } ?: throw Exception("Sign In with Google failed")
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Sign in with Google failed")
        }
    }

    //We launched the login UI
    fun signInWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    //We evaluate the auth result. If result is Successful, we execute the function onCreated(). If not, we show the error
    //The function onCreated() will trigger an alert asking the user to enter a username
    suspend fun anonymouslySignIn(
        context: Context,
        onCreated: () -> Unit
    ) {
        when (val result = signInAnonymously()) {
            is AuthRes.Success -> {
                onCreated()
            }

            is AuthRes.Error -> {
                Toast.makeText(context, result.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

}