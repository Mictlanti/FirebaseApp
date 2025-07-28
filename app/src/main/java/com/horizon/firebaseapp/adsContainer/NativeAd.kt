package com.horizon.firebaseapp.adsContainer

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.horizon.firebaseapp.R
import com.horizon.firebaseapp.components.BodySmall

class NativeAdmob {

    val unitId = "ca-app-pub-3940256099942544/2247696110"

    fun loadNativeAd(context: Context, builder: (NativeAd) -> Unit)  {
        val adLoader = AdLoader.Builder(context, unitId)
            .forNativeAd { ad ->
                builder(ad)
            }
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

}

@Composable
fun NativeAdLoader() {

    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    val context = LocalContext.current
    val nativeAdmob = remember { NativeAdmob() }

    DisposableEffect(Unit) {
        nativeAdmob.loadNativeAd(context) { ad -> nativeAd = ad }
        onDispose {
            nativeAd?.destroy()
        }
    }

    if(nativeAd != null) {
        AddCard(nativeAd) {
            loadNativeAd(context) {  }
        }
    } else {
        BodySmall(text = "Loading Ad...")
    }

}

@Composable
fun AddCard(nativeAd: NativeAd?, onClick: () -> Unit) {
    Card {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            AsyncImage(
                model = nativeAd?.icon?.uri,
                contentDescription = "Ad icon",
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier.weight(1f)) {
                nativeAd?.headline?.let { BodySmall(text = it) }
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFDCE775), shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        BodySmall(text = "Ad")
                    }
                    Spacer(Modifier.width(8.dp))
                    Row {
                        nativeAd?.starRating?.let {
                            repeat(it.toInt()) {
                                Icon(
                                    Icons.Default.Star,
                                    "Rating",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    //NativeAdCAllToActionView(Modifier.padding(5.dp)) { NaviteButton(text = callToAction) }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4)),
                modifier = Modifier.height(40.dp)
            ) {
                nativeAd?.callToAction?.let { BodySmall(text = it) }
            }
        }
    }
}

fun loadNativeAd(context: Context, onAdLoaded: (NativeAd) -> Unit) {
    val adLoader =
        AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { nativeAd -> onAdLoaded(nativeAd) }
            .withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e(TAG, "Native ad failed to load: ${error.message}")
                    }

                    override fun onAdLoaded() {
                        Log.d(TAG, "Native ad was loaded.")
                    }

                    override fun onAdImpression() {
                        Log.d(TAG, "Native ad recorded an impression.")
                    }

                    override fun onAdClicked() {
                        Log.d(TAG, "Native ad was clicked.")
                    }
                }
            )
            .build()
    adLoader.loadAd(AdRequest.Builder().build())
}