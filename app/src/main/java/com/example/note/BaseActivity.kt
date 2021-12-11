package com.example.note

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.note.wp.Wallpaper
import com.example.note.wp.WpModel
import com.example.note.wp.wpModel

open class BaseActivity : AppCompatActivity(), Wallpaper {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        wpModel.getSp(this, WpModel.KEY).apply {
//            if (this != WpModel.INVILD_VALUE) getWindow().setBackgroundDrawableResource(this)
//        }
        wpModel.wpListener.add(this)
        Log.d("AAA", "onCreate: ${wpModel.wpListener.size}")
        setNightMode()
    }

    //设置主题
    fun setNightMode() {
        setTheme(R.style.DayTheme)
    }

    override fun onWallpaperChanged(wallPaperResId: Int) {
        Log.d("aaaaa", "onWallpaperChanged: $this $wallPaperResId")
        getWindow().setBackgroundDrawableResource(wallPaperResId)
    }

    val isNightMode: Boolean
        get() {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
            return sharedPreferences.getBoolean("nightMode", false)
        }
}