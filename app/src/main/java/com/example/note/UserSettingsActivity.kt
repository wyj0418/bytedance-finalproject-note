package com.example.note

import android.annotation.SuppressLint
import com.example.note.BaseActivity
import android.content.SharedPreferences
import android.os.Bundle
import com.example.note.R
import android.preference.PreferenceManager
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import java.lang.Boolean.getBoolean

class UserSettingsActivity : BaseActivity() {
    private var nighMode: Switch? = null
    private var sharedPreferences: SharedPreferences? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preference_layout)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val intent = intent
        initView()
        val myToolbar = findViewById<View>(R.id.myToolbar) as Toolbar
        setSupportActionBar(myToolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        if (isNightMode) myToolbar.navigationIcon =
            getDrawable(R.drawable.ic_baseline_settings_24_pink) else myToolbar.navigationIcon =
            getDrawable(R.drawable.ic_baseline_settings_24)

        findViewById<View>(R.id.change_wall).setOnClickListener{
            val intent=Intent(this, com.example.note.wallpaper::class.java)
            startActivity(intent)
        }
    }

    fun initView() {

        nighMode = findViewById(R.id.nightMode)
        with(nighMode) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
            this!!.setChecked(sharedPreferences!!.getBoolean("nightMode", false))
            this!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                setNightModePref(isChecked)
                setSelfNightMode()
            })
        }
    }

    private fun setNightModePref(night: Boolean) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val editor = sharedPreferences!!.edit()
        editor.putBoolean("nightmode", night)
        editor.commit()
    }

    private fun setSelfNightMode() {
        super.setNightMode()
        val intent = Intent(this, UserSettingsActivity::class.java)
        //startActivity(intent)
        finish()
    }
}