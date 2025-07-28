package com.horizon.firebaseapp.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.horizon.firebaseapp.data.AuthManager
import com.horizon.firebaseapp.navigation.AppScreens
import com.horizon.firebaseapp.viewmodel.AuthViewModel
import com.horizon.firebaseapp.viewmodel.RemoteVM

@Composable
fun CardsHome(
    text: String,
    imageVector: ImageVector,
    destination: AppScreens,
    navController: NavController
) {
    ElevatedCard(
        onClick = {
            navController.navigate(destination.route)
        },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                25.dp,
                alignment = Alignment.CenterVertically
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Icon(
                imageVector,
                text,
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(50.dp)
            )
            BodySmall(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHome(onClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { HeadLineLarge(text = "Home View") },
        actions = {
            ActionIconHome { onClick() }
        }
    )
}

@Composable
private fun ActionIconHome(onClick: () -> Unit) {
    IconButton(onClick = { onClick() }) {
        Icon(
            Icons.Default.PersonPin,
            "Sign out",
            tint = MaterialTheme.colorScheme.error,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignOutAlert(
    show: Boolean,
    onDismissRequest: () -> Unit,
    authManager: AuthManager,
    navController: NavController
) {

    //We created an alert to sign out with two options: Confirm or Cancel
    val confirm = listOf("Confirm", "Cancel")

    if (show) {
        BasicAlertDialog(
            onDismissRequest = { onDismissRequest() },
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = .9f),
                shape = MaterialTheme.shapes.extraLarge
            )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(20.dp)
            ) {
                HeadLineLarge(text = "Sign out", color = MaterialTheme.colorScheme.onError)
                BodyMedium(
                    text = "Are you sure you want to sign out?",
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    confirm.forEachIndexed { index, s ->
                        //If user click confirm button, sign out, else we remove the alert
                        ButtonAlert(
                            index,
                            s,
                            Modifier.weight(1f)
                        ) {
                            if (index == 0) {
                                authManager.singOut(navController)
                            } else {
                                onDismissRequest()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonAlert(index: Int, text: String, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (index == 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        BodySmall(
            text = text,
            color = if (index == 0) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSecondary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    isOpenProfile: Boolean,
    authViewModel: AuthViewModel,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit
) {

    val username by authViewModel.userState.collectAsState()
    val error = authViewModel.error

    LaunchedEffect(Unit) {
        authViewModel.getUser()
    }

    if (isOpenProfile) {
        ModalBottomSheet(
            onDismissRequest,
            dragHandle = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 30.dp)
                ) {
                    BodyLarge(text = "Your Profile", color = MaterialTheme.colorScheme.onSecondary)
                    BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.onSecondary)
                }
            },
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 50.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BodySmall(text = "username:")
                    if (error.error) BodySmall(
                        text = username.userId,
                        color = MaterialTheme.colorScheme.error
                    ) else BodyLarge(
                        text = username.userName,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                OutlinedButton(
                    onClick = { onClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BodyMedium(text = "Sign out", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertUpdate(remoteVM: RemoteVM) {

    val ctx = LocalContext.current

    if (remoteVM.showUpdateDialog) {
        BasicAlertDialog(
            onDismissRequest = {
                if (remoteVM.forceUpdate) {
                    remoteVM.showUpdateDialog = false
                }
            },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    20.dp,
                    alignment = Alignment.CenterVertically
                ),
                modifier = Modifier.padding(20.dp)
            ) {
                BodyLarge(text = "Update Available")
                BodyMedium(text = "New version available in play store, Do you want to update?")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Update", "Cancel").forEachIndexed { index, s ->
                        ButtonAlert(
                            index,
                            s,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (index == 0) {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.horizon.firebaseapp")
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                ctx.startActivity(intent)
                            } else {
                                if (!remoteVM.forceUpdate) {
                                    remoteVM.showUpdateDialog = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}