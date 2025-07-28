package com.horizon.firebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.ads.MobileAds
import com.horizon.firebaseapp.adsContainer.OpenAdManager
import com.horizon.firebaseapp.navigation.Navigation
import com.horizon.firebaseapp.ui.theme.FirebaseAppTheme
import com.horizon.firebaseapp.viewmodel.AuthViewModel
import com.horizon.firebaseapp.viewmodel.NotesViewModel
import com.horizon.firebaseapp.viewmodel.RemoteVM

class MainActivity : ComponentActivity() {

    private lateinit var appOpenManager: OpenAdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}
        appOpenManager = OpenAdManager()

        setContent {

            val notesVM : NotesViewModel = viewModel()
            val authVM : AuthViewModel = viewModel()
            val remoteVM : RemoteVM = viewModel()

            LaunchedEffect(Unit) {
                remoteVM.fetchRemoteConfig()
            }

            FirebaseAppTheme {
                Navigation(notesVM, authVM, remoteVM)
            }

        }
    }

    override fun onPause() {
        super.onPause()
        appOpenManager.loadAd(this)
    }

    override fun onRestart() {
        super.onRestart()
        appOpenManager.showAd(this)
    }
}
