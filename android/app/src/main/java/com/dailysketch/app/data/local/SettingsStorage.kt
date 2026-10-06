package com.dailysketch.app.data.local

import android.content.Context
import android.content.SharedPreferences

class SettingsStorage(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun readPack(): String = preferences.getString(KEY_PACK, DEFAULT_PACK).orEmpty()

    fun writePack(value: String) {
        preferences.edit().putString(KEY_PACK, value).apply()
    }

    fun readReminder(): Boolean = preferences.getBoolean(KEY_REMINDER, true)

    fun writeReminder(value: Boolean) {
        preferences.edit().putBoolean(KEY_REMINDER, value).apply()
    }

    fun readLastOpened(): String = preferences.getString(KEY_LAST_OPENED, "").orEmpty()

    fun writeLastOpened(value: String) {
        preferences.edit().putString(KEY_LAST_OPENED, value).apply()
    }

    private companion object {
        const val PREFS_NAME = "daily_sketch_prefs"
        const val KEY_PACK = "prompt_pack"
        const val KEY_REMINDER = "reminder_mark"
        const val KEY_LAST_OPENED = "last_opened_iso"
        const val DEFAULT_PACK = "mixed"
    }
}
