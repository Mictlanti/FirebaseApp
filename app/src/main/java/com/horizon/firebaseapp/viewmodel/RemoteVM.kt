package com.horizon.firebaseapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.horizon.firebaseapp.data.constants.Constants.VERSION_CODE
import kotlinx.coroutines.launch

class RemoteVM() : ViewModel() {

    //Instance of Firebase Remote Config
    private val remoteConfig = Firebase.remoteConfig

    var showUpdateDialog by mutableStateOf(false) // Flag to show the update dialog

    var forceUpdate by mutableStateOf(false) // Flag to check if an update is forced
        private set //This flag is only editable inside the ViewModel

    fun fetchRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60 // Minimum observation interval
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(
            mapOf(
                "force_update" to false, // Default value for "force_update"
                "latest_version" to VERSION_CODE // Default value for "latest_version"
            )
        )

        remoteConfig.fetchAndActivate() //We get the values of the remote config
            .addOnCompleteListener { task ->
                //If the task is successful, we check the app version
                if (task.isSuccessful) {
                    checkAppVersion()
                }
            }
    }

    private fun checkAppVersion() {
        val latestVersion = remoteConfig.getString("latest_version").toInt() //From firebase, we get the latest version
        val force = remoteConfig.getBoolean("force_update") //From firebase, we get the flag to check if an update is forced
        val currentVersionCode = VERSION_CODE //From constants, we get the current version

        //If the app version is less than the latest version, we show the update dialog
        if (latestVersion > currentVersionCode) {
            forceUpdate = force
            showUpdateDialog = true
        }
    }

}