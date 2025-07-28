package com.horizon.firebaseapp.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey

object RewardKeys {
    val LAST_REWARD_DATE = stringPreferencesKey("last_reward_date")
    val REWARD_AMOUNT = stringPreferencesKey("reward_amount")
}

class RewardManager(context: Context) {



}