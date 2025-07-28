package com.horizon.firebaseapp.views.login

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import com.horizon.firebaseapp.R
import com.horizon.firebaseapp.components.BodyLarge
import com.horizon.firebaseapp.components.BodyMedium
import com.horizon.firebaseapp.components.BodySmall
import com.horizon.firebaseapp.components.ButtonAlert
import com.horizon.firebaseapp.components.HeadLineLarge
import com.horizon.firebaseapp.components.LogButtons
import com.horizon.firebaseapp.data.AuthManager
import com.horizon.firebaseapp.data.AuthRes
import com.horizon.firebaseapp.navigation.AppScreens
import com.horizon.firebaseapp.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    authManager: AuthManager,
    authVM: AuthViewModel,
    navController: NavController
) {

    val logList = listOf(
        R.drawable.icons8_logo_de_google,
        R.drawable.icon_inc
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleSignInLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (val account =
                authManager.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
                is AuthRes.Success -> {
                    val credential = GoogleAuthProvider.getCredential(account.data.idToken, null)
                    scope.launch {
                        val fireUser = authManager.signInWithGoogleCredential(credential)
                        if (fireUser != null) {
                            Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
                            navController.navigate(AppScreens.HomeScreen.route)
                        }
                    }
                }

                is AuthRes.Error -> {
                    Toast.makeText(context, "Error: ${account.errorMessage}", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
                }
            }
        }

    /*
    We need to implementation this libs:

    googleGmsGoogleServices = "4.4.2"
    firebaseAuth = "23.1.0"
    firebaseAuthKtx = "23.1.0"
    playServicesAuth = "21.2.0"
    googleid = "1.1.1"
    googleServices = "4.4.2"

    firebase-auth = { module = "com.google.firebase:firebase-auth", version.ref = "firebaseAuth" }
    firebase-auth-ktx = { module = "com.google.firebase:firebase-auth-ktx", version.ref = "firebaseAuthKtx" }
    google-services = { module = "com.google.gms:google-services", version.ref = "googleServices" }
    play-services-auth = { module = "com.google.android.gms:play-services-auth", version.ref = "playServicesAuth" }
    googleid = { group = "com.google.android.libraries.identity.googleid", name = "googleid", version.ref = "googleid" }
    */

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(
                200.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadLineLarge(text = "FirebaseApp")
            BodyLogAnimation(text = "In this project, we used firebase service")
            SignInLog(
                logList,
                authManager,
                authVM,
                scope,
                context,
                navController,
                googleSignInLauncher
            )
        }
    }

    /*
    For end authentication with google, we need the "Keytool" on the certificate.
    Steps:
    - In Android Studio, in the right part of screen, we look different options (notifications, gradle, device manager...)
    - Click in "gradle" and search the option "Execute Gradle Task".
    - A window that says "Run Anything" will appear. In then, we write "signinReport"
    - The terminal will run this command and then display a list of information, among them will be "Sha-1".
    - Now we go to firebase console and we created a new project. Sync your app with the project on Firebase.
    - Enable Google Sign-in and Anonymous Sign-in.
    - In the "Edit configuration" in authentication with google there will be two things:
        -> SDK web configuration: Here we get the "web client ID"
        -> Click in "Project configuration": Here, in the bottom screen, we will find the option "Add digital footprint". Copy SHA-1 in the android studio and paste it here.

     * Now you’re ready to integrate Firebase Auth with Google Sign-In and anonymous login.
    */
}

@Composable
private fun BodyLogAnimation(text: String) {

    var textToShow by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        for (i in text.indices) {
            textToShow = text.take((i + 1))
            delay(300)
        }
    }

    BodyMedium(text = textToShow, textAlign = TextAlign.Center)
}

@Composable
private fun SignInLog(
    logList: List<Int>,
    authManager: AuthManager,
    authVM: AuthViewModel,
    scope: CoroutineScope,
    context: Context,
    navController: NavController,
    googleSignInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {

    val userName = remember { mutableStateOf("") }
    val isOpen = remember { mutableStateOf(false) }

    AlertUsername(
        isOpen.value,
        userName,
        authVM,
        navController,
        context
    ) {
        isOpen.value = false
    }

    Box {
        Column {
            BodySmall(text = "Sign in:")
            Spacer(Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                logList.forEachIndexed { index, i ->
                    LogButtons(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        i
                    ) {
                        when (index) {
                            0 -> authManager.signInWithGoogle(googleSignInLauncher)
                            1 -> {
                                scope.launch {
                                    authManager.anonymouslySignIn(context) {
                                        isOpen.value = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertUsername(
    isOpen: Boolean,
    usernameMutable: MutableState<String>,
    authVM: AuthViewModel,
    navController: NavController,
    ctx: Context,
    onDismiss: () -> Unit
) {

    val listOptions = listOf("Confirm", "Cancel")
    val scope = rememberCoroutineScope()

    if (isOpen) {
        BasicAlertDialog(onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    20.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {
                BodyLarge(text = "Selected a username")
                TextField(
                    value = usernameMutable.value,
                    onValueChange = { usernameMutable.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(20.dp))
                Row {
                    listOptions.forEachIndexed { index, s ->
                        ButtonAlert(
                            index,
                            s,
                            Modifier.weight(1f)
                        ) {
                            //when the user choose an username and click the "confirm" button, navigation to "HomeScreen"; else, the alert vanish
                            if (index == 0) {
                                scope.launch {
                                    val success = authVM.saveUser(usernameMutable.value)
                                    if (success) {
                                        navController.navigate(AppScreens.HomeScreen.route)
                                    } else {
                                        Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            else {
                                onDismiss()
                            }
                        }
                    }
                }
            }
        }
    }
}