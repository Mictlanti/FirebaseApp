package com.horizon.firebaseapp.adsContainer

/*
* Add dependencies in gradle *
*
* [versions]
* playServicesAds = "23.5.0"
* [libraries]
* play-services-ads = { module = "com.google.android.gms:play-services-ads", version.ref = "playServicesAds" }
*
* Build gradle
*
* implementation(libs.play.services.ads)
*
* Read documentation in https://developers.google.com/admob/android/quick-start
*
* In AndroidManifest.xml add:
*<application>
    <!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>
  </application>
*
* To test banner ads use:
* ca-app-pub-3940256099942544/9214589741
*
* To test interstitial ads use:
* ca-app-pub-3940256099942544/1033173712
*
* To test rewarded ads use:
* ca-app-pub-3940256099942544/5224354917
*
* To test native ads use:
* ca-app-pub-3940256099942544/2247696110
*
* To test App open ads use:
* ca-app-pub-3940256099942544/9257395921
*/