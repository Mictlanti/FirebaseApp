package com.horizon.firebaseapp.adsContainer

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun InterstitialAdsContainer() {

    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    val btnText = remember { mutableStateOf("Loading...") }
    val btnEnabled = remember { mutableStateOf(false) }
    val adUnitId = "ca-app-pub-3940256099942544/1033173712"
    val context = LocalContext.current

    //We loaded the ad to Admob
    fun loadInterstitialAd() {
        //We start with a trial ID
        InterstitialAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            //We handle two events: onAdFailedToLoad and onAdLoaded
            object : InterstitialAdLoadCallback() {
                //If the ad fails to load, we set the ad to null
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e("InterstitialAd", loadAdError.toString())
                    interstitialAd = null
                    btnText.value = "Error to load ad"
                    btnEnabled.value = true
                }

                //Else, we load the ad
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    btnText.value = "Continue"
                    btnEnabled.value = true
                }
            }
        )
    }

    //If the ad is loaded, we show it
    fun showInterstitialAd(onAdDismissed: () -> Unit) {
        //Is necessary to cast the context to Activity
        val activity = context as? Activity
        if (activity == null) {
            Toast.makeText(context, "Activity context not available", Toast.LENGTH_SHORT).show()
            return
        }

        interstitialAd?.let { ad ->
            //We set the full screen content callback
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                //If the ad fails to show, recharge the ad
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    btnText.value = "Error to show ad"
                    btnEnabled.value = false
                    loadInterstitialAd()
                }

                //If it close,  recharge, button enabled, change the text and execute the action onAdDismissed()
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitialAd()
                    onAdDismissed()
                    btnText.value = "loading..."
                    btnEnabled.value = false
                }
            }
            //If the ad is loaded, we show it
            ad.show(activity)
        } ?: run {
            //If the ad is not loaded, we show a toast
            Toast.makeText(context, "Ad not available", Toast.LENGTH_SHORT).show()
        }
    }

    //When the composable is loaded, we load the ad
    DisposableEffect(Unit) {
        loadInterstitialAd()
        onDispose {  }
    }

    Button(
        enabled = btnEnabled.value,
        onClick = {
            showInterstitialAd {
                Toast.makeText(context, "Interstitial Ad Shown!", Toast.LENGTH_SHORT).show()
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = btnText.value)
    }
}