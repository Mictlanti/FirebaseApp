package com.horizon.firebaseapp.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.horizon.firebaseapp.components.AlertUpdate
import com.horizon.firebaseapp.components.CardsHome
import com.horizon.firebaseapp.components.ProfileBottomSheet
import com.horizon.firebaseapp.components.SignOutAlert
import com.horizon.firebaseapp.components.TopBarHome
import com.horizon.firebaseapp.data.AuthManager
import com.horizon.firebaseapp.navigation.AppScreens
import com.horizon.firebaseapp.viewmodel.AuthViewModel
import com.horizon.firebaseapp.viewmodel.RemoteVM

@Composable
fun HomeRoute(
    navController: NavController,
    authManager: AuthManager,
    authViewModel: AuthViewModel,
    remoteVM: RemoteVM
) {

    val listNavi = listOf(
        "Analytics" to AppScreens.Analytics,
        "Admob" to AppScreens.AdmobScreen,
        "Cloud" to AppScreens.CloudScreen,
        "Storage" to AppScreens.StorageScreen,
        "Remote Config" to AppScreens.RemoteConfigScreen,
        "Cloud Messaging" to AppScreens.CloudMessagingScreen,
        "Crashlytics" to AppScreens.CrashlyticsScreen,
        "AI logic" to AppScreens.AILogicScreen
    )
    val icons = listOf(
        Icons.Default.Analytics,
        Icons.Default.Money,
        Icons.Default.Cloud,
        Icons.Default.Storage,
        Icons.Default.SettingsRemote,
        Icons.AutoMirrored.Filled.Message,
        Icons.Default.Error,
        Icons.Default.SettingsSuggest
    )
    val showAlert = remember { mutableStateOf(false) }
    val openProfile = remember { mutableStateOf(false) }

    SignOutAlert(
        showAlert.value,
        onDismissRequest = { showAlert.value = false },
        authManager,
        navController
    )

    ProfileBottomSheet(
        openProfile.value,
        authViewModel,
        onClick = { showAlert.value = true }
    ) { openProfile.value = false }

    AlertUpdate(remoteVM)

    Scaffold(
        topBar = {
            TopBarHome() {
                openProfile.value = true
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                20.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            LazyVerticalGrid(
                GridCells.Fixed(2),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center
            ) {
                itemsIndexed(listNavi) { index, (text, destination) ->
                    CardsHome(
                        text,
                        icons[index],
                        destination,
                        navController
                    )
                }
            }
        }

    }
}