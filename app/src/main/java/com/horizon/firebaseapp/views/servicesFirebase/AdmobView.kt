package com.horizon.firebaseapp.views.servicesFirebase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.horizon.firebaseapp.adsContainer.BannerAd
import com.horizon.firebaseapp.adsContainer.InterstitialAdsContainer
import com.horizon.firebaseapp.adsContainer.LoadingRewardedAd
import com.horizon.firebaseapp.adsContainer.NativeAdLoader
import com.horizon.firebaseapp.components.BodyMedium
import com.horizon.firebaseapp.components.TopAppBarViewFB

@Composable
fun AdmobRoute(navController: NavController) {

    var reward by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBarViewFB("AdMob Container", navController){
                BodyMedium(text = reward.toString())
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            BannerAd()
            InterstitialAdsContainer()
            NativeAdLoader()
            LoadingRewardedAd() { reward++ }
        }
    }
}