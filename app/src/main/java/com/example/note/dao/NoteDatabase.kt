package com.example.note.dao

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import com.example.note.dao.NoteDatabase
import com.example.note.dao.CRUD
import android.widget.BaseAdapter
import com.example.note.adapter.NoteAdapter.MyFilter
import android.view.ViewGroup
import android.preference.PreferenceManager
import com.example.note.R
import android.widget.TextView
import android.widget.Filter.FilterResults
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.note.BaseActivity
import android.widget.EditText
import com.example.note.translate.TranslateActivity
import android.app.Activity
import android.content.*
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

class NoteDatabase(context: Context?) : SQLiteOpenHelper(context, "notes", null, 1) {
    companion object {
        const val TABLE_NAME = "notes"
        const val CONTENT = "content"
        const val ID = "_id"
        const val TIME = "time"
        const val MODE = "mode"
        const val IMAGE = "image"
        const val VIDEO = "video"
    }

    //创建数据库
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + TABLE_NAME + "(" + ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CONTENT + " TEXT NOT NULL, "
                    + TIME + " TEXT NOT NULL, "
                    + MODE + " INTEGER DEFAULT 1,"
                    + IMAGE + " TEXT,"
                    + VIDEO + " TEXT)"
        )
    }

    /**
     * 检测已有的版本
     *
     * @param db         指向的数据库
     * @param oldVersion 检测已有的版本
     * @param newVersion 新版本
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //从最小的版本开始，向最新的版本循环，保证每一步都更新
        /* for (int i = oldVersion; i < newVersion; i++) {
            switch (i){
                case 1:break;
                case 2:updateMode(db);
                default:break;
            }
        }*/
    }

}