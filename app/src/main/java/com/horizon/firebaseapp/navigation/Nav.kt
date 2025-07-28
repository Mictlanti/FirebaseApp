package com.horizon.firebaseapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseUser
import com.horizon.firebaseapp.data.AuthManager
import com.horizon.firebaseapp.viewmodel.AuthViewModel
import com.horizon.firebaseapp.viewmodel.NotesViewModel
import com.horizon.firebaseapp.viewmodel.RemoteVM
import com.horizon.firebaseapp.views.HomeRoute
import com.horizon.firebaseapp.views.login.LoginRoute
import com.horizon.firebaseapp.views.servicesFirebase.AdmobRoute
import com.horizon.firebaseapp.views.servicesFirebase.CloudData.AdNotesView
import com.horizon.firebaseapp.views.servicesFirebase.CloudData.EditNoteRoute
import com.horizon.firebaseapp.views.servicesFirebase.CloudData.NotesDatabase
import com.horizon.firebaseapp.views.servicesFirebase.RemoteConfigRoute

@Composable
fun Navigation(notesVM: NotesViewModel, authVM: AuthViewModel, remoteVM: RemoteVM) {

    val navController = rememberNavController()
    val context = LocalContext.current
    val authManager = AuthManager(context)
    val user: FirebaseUser? = authManager.getCurrentUser()

    NavHost(
        navController,
        startDestination = if(user != null) AppScreens.HomeScreen.route else AppScreens.LogInScreen.route
    ) {
        composable(AppScreens.LogInScreen.route){
            LoginRoute(authManager, authVM, navController)
        }
        composable(AppScreens.HomeScreen.route) {
            HomeRoute(navController, authManager, authVM, remoteVM)
        }
        composable(AppScreens.CloudScreen.route) {
            NotesDatabase(navController, notesVM)
        }
        composable(AppScreens.NotesScreen.route) {
            AdNotesView(navController, notesVM)
        }
        composable(
            AppScreens.EditNoteScreen.route + "/{idDoc}",
            arguments = listOf(
                navArgument("idDoc") { type = NavType.StringType }
            )
        ) {
            val idDoc = it.arguments?.getString("idDoc") ?: ""
            EditNoteRoute(navController, notesVM, idDoc)
        }
        composable(AppScreens.AdmobScreen.route) {
            AdmobRoute(navController)
        }
        composable(AppScreens.RemoteConfigScreen.route) {
            RemoteConfigRoute(navController)
        }
    }

}