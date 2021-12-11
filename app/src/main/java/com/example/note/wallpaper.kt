package com.example.note

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageView
import com.example.note.wp.WpModel
import com.example.note.wp.wpModel


class wallpaper : BaseActivity() {
    private lateinit var imageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper)

        findViewById<View>(R.id.wall_a).setOnClickListener {

            window.setBackgroundDrawableResource(R.mipmap.img_1)
//            var sharedPreferences = getSharedPreferences("bg_a", MODE_PRIVATE)
//            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//            editor.putString("name", "A")
//            editor.commit()

            // modify
            wpModel.putSp(
                WpModel.KEY,
                R.mipmap.img_1
            )
        }
        findViewById<View>(R.id.wall_b).setOnClickListener {

            window.setBackgroundDrawableResource(R.mipmap.img_2)
            wpModel.putSp(
                WpModel.KEY,
                R.mipmap.img_2
            )
        }

    }
}
