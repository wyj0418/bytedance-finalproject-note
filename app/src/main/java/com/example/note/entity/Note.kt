package com.example.note.entity

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import com.example.note.dao.NoteDatabase
import com.example.note.dao.CRUD
import android.widget.BaseAdapter
import com.example.note.adapter.NoteAdapter.MyFilter
import android.view.ViewGroup
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.note.R
import android.widget.TextView
import android.widget.Filter.FilterResults
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.note.BaseActivity
import android.widget.EditText
import android.content.Intent
import com.example.note.translate.TranslateActivity
import android.app.Activity
import android.content.DialogInterface
import android.widget.AdapterView.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.note.adapter.NoteAdapter
import android.widget.PopupWindow
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.view.WindowManager
import android.util.DisplayMetrics
import com.example.note.EditActivity
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View.OnTouchListener
import android.view.MotionEvent
import android.widget.AdapterView

class Note {
    var id //主键
            : Long = 0
    var content //笔记内容
            : String? = null
    var time //笔记时间
            : String? = null
    var tog //笔记标签
            = 0
    var image //图片路径
            : String? = null
    var video //视频路径
            : String? = null

    constructor() {}
    constructor(content: String?, time: String?, tog: Int, image: String?, video: String?) {
        this.content = content
        this.time = time
        this.tog = tog
        this.image = image
        this.video = video
    }

    override fun toString(): String {
        return """$content
${time!!.substring(5, 16)} $id"""
    }
}