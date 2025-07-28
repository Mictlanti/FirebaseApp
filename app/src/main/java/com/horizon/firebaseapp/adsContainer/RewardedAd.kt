package com.horizon.firebaseapp.adsContainer

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.horizon.firebaseapp.components.BodyMedium

class AdmobRewardedAd {

    private var rewardedAd: RewardedAd? = null

    fun loadRewardedAd(context: Context) {

        val adRequest = AdRequest.Builder().build()
        val adId = "ca-app-pub-3940256099942544/5224354917"
        val activity = context as Activity

        RewardedAd.load(activity, adId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                rewardedAd = null
                Toast.makeText(activity, "Ad failed to load", Toast.LENGTH_SHORT).show()
            }

            override fun onAdLoaded(p0: RewardedAd) {
                super.onAdLoaded(p0)
                rewardedAd = p0
                Toast.makeText(activity, "Ad loaded", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showRewardedAd(context : Context, onRewardedEarned: () -> Unit) {

        val activity = context as Activity

        if (rewardedAd == null) {
            Toast.makeText(activity, "Ad not available", Toast.LENGTH_SHORT).show()
            return
        }

        rewardedAd?.show(activity) {
            Toast.makeText(activity, "Ad showed", Toast.LENGTH_SHORT).show()
            onRewardedEarned()
        }

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
            }
        }
    }
}

@Composable
fun LoadingRewardedAd(onRewardedEarned: () -> Unit) {

    val ctx = LocalContext.current
    val reward : AdmobRewardedAd = remember { AdmobRewardedAd() }

    DisposableEffect(Unit) {
        reward.loadRewardedAd(ctx)
        onDispose {  }
    }

    Button(
        onClick = {
            reward.showRewardedAd(ctx) {
                onRewardedEarned()
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BodyMedium(text = "Show rewarded ad")
    }
}