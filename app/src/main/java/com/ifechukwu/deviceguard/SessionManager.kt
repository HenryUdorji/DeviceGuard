package com.ifechukwu.deviceguard

import android.content.SharedPreferences

/**
 * @Author: ifechukwu.udorji
 * @Date: 12/31/2024
 */
class SessionManager(
    private val sharedPreferences: SharedPreferences
) {
    fun saveFirebaseToken(token: String?) {
        sharedPreferences.edit().putString(PREF_FIREBASE_TOKEN, token).apply()
    }

    fun getFirebaseToken(): String? {
        return sharedPreferences.getString(PREF_FIREBASE_TOKEN, null)
    }

    companion object {
        const val PREF_FIREBASE_TOKEN = "PREF_FIREBASE_TOKEN"
    }
}