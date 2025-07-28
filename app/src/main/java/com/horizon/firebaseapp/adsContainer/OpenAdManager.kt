package com.horizon.firebaseapp.adsContainer

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

private const val TEST_AD_ID = "ca-app-pub-3940256099942544/9257395921"
private const val TAG = "OpenAdManager"

class OpenAdManager {

    private var loadTime: Long = 0
    var opeAd: AppOpenAd? = null
    private var isLoading = false
    var isShowingAd = false

    fun loadAd(context: Context) {

        if (isLoading || isAdAvailable() || isShowingAd) {

        }

        isLoading = true

        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            TEST_AD_ID,
            request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    super.onAdLoaded(ad)
                    Log.i(TAG, "Ad was loaded")
                    opeAd = ad
                    isLoading = false
                    loadTime = Date().time
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    Log.i(TAG, error.message)
                    isLoading = false
                }
            }
        )
    }

    private fun wasLoadTimeLessThanHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3_600_000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    private fun isAdAvailable(): Boolean {
        return opeAd != null && wasLoadTimeLessThanHoursAgo(4)
    }

    fun showAd(activity: Activity) {
        if (isShowingAd) {
            Log.i(TAG, "Ad already showing")
            return
        }

        if (!isAdAvailable()) {
            Log.i(TAG, "Ad not available")
            loadAd(activity)
            return
        }

        opeAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdDismissedFullScreenContent() {
                Log.i(TAG, "Ad dismissed full screen content")
                opeAd = null
                isShowingAd = false
                loadAd(activity)
            }

            override fun onAdShowedFullScreenContent() {
                Log.i(TAG, "Ad showed full screen content")
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.i(TAG, error.message)
                isShowingAd = false
                opeAd = null
                loadAd(activity)
            }
        }

        isShowingAd = true
        opeAd?.show(activity)

    }
}