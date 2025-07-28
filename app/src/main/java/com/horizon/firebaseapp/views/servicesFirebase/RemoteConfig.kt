package com.horizon.firebaseapp.views.servicesFirebase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.horizon.firebaseapp.components.BodyLarge
import com.horizon.firebaseapp.components.BodySmall
import com.horizon.firebaseapp.components.TopAppBarViewFB

@Composable
fun RemoteConfigRoute(navController: NavController) {

    val explainRemote = listOf(
        "You need this implementation in your gradle.kts" +
                "\nimplementation(libs.firebase.config)" +
                "In libs:" +
                "firebase-config = { group = \"com.google.firebase\", name = \"firebase-config\", version.ref = \"firebaseConfig\" }",
        "In this case, we show an alert dialog that say \"Update Available\", if we clicked in confirm, we go to play store;" +
                " else, we remove the alert dialog",
        "In Firebase, we create a two files in remote config that say \"force_update\" and \"latest_version\"" +
                "this files has a default value \"false\" and \"2\", respectively",
        "If we change the \"latest_version\" to 3, we show the alert dialog because the version of our project is 2",
        "If we change the \"force_update\" to true, we show the alert dialog too, but now we not remove the alert dialog"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBarViewFB("Remote Config", navController)
        }
    ) { innerPad ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPad)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                BodyLarge(text = "In this view, we show how to use Remote Config")
            }
            itemsIndexed(explainRemote) { index, s ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BodyLarge(text = "${index + 1} -")
                    BodySmall(text = s)
                }
            }
        }
    }
}