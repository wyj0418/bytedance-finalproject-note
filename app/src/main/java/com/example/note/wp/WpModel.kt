package com.example.note.wp

import android.content.Context
import android.preference.PreferenceManager
import org.koin.java.KoinJavaComponent

inline val wpModel: WpModel
    get() = KoinJavaComponent.getKoin().get()

class WpModel(
    val context: Context
) {

    companion object {
        const val KEY = "wallpaper_key"
        const val INVILD_VALUE = -1
    }

    val wpListener = mutableListOf<Wallpaper>()

    fun putSp(
        key: String,
        value: Int
    ) {
        PreferenceManager.getDefaultSharedPreferences(
            context.applicationContext
        ).edit().apply {
           putInt(key, value)
            apply()
        }

        for (listener in wpListener) {
            listener.onWallpaperChanged(value)
        }
    }

    fun getSp(
        key: String
    ): Int = PreferenceManager.getDefaultSharedPreferences(context.applicationContext).getInt(key, INVILD_VALUE)
}