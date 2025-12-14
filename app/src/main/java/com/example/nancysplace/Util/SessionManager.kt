package com.example.nancysplace.Util

import android.content.Context

object SessionManager {
    private const val PREF = "nancysplace_prefs"
    private const val KEY_LOGGED = "logged"

    fun setLogged(context: Context, value: Boolean) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_LOGGED, value)
            .apply()
    }

    fun isLogged(context: Context): Boolean {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getBoolean(KEY_LOGGED, false)
    }

    fun logout(context: Context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_LOGGED, false)
            .apply()
    }
}