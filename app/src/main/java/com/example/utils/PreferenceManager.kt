package com.example.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {

    private const val PREFS_NAME = "my_prefs"


    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveString(key: String, value: String, context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, context: Context): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(key, null)
    }
}