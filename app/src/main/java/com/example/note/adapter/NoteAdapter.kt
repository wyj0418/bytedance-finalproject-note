package com.example.note.adapter

import android.content.Context
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.note.R
import com.example.note.entity.Note
import java.util.*

class NoteAdapter(//上下文
    private val mContext: Context, noteList: MutableList<Note>
) : BaseAdapter(), Filterable {
    private val backList //用来备份原始数据
            : MutableList<Note>
    private var noteList //当前的笔记
            : List<Note>
    private var myFilter: MyFilter? = null
    override fun getCount(): Int {
        return noteList.size
    }

    override fun getItem(position: Int): Any {
        return noteList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            mContext
        )
        mContext.setTheme(R.style.DayTheme)
        //定义视图，获取组件
        val view = View.inflate(mContext, R.layout.note_layout, null)
        val tv_content = view.findViewById<TextView>(R.id.tv_content)
        val tv_time = view.findViewById<TextView>(R.id.tv_time)

        //获取文本内容，并赋值
        val allText = noteList[position].content
        val time = noteList[position].time
        tv_content.text = allText
        tv_time.text = time

        //保存笔记的主键
        view.tag = noteList[position].id
        return view
    }

    override fun getFilter(): Filter {
        if (myFilter == null) {
            myFilter = MyFilter()
        }
        return myFilter!!
    }

    internal inner class MyFilter : Filter() {
        //定义过滤规则
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            val list: MutableList<Note>
            //当过滤的关键字为空的时候，显示所有的数据
            if (TextUtils.isEmpty(constraint)) {
                list = backList
            } else { //否则把符合条件的数据对象添加到集合中
                list = ArrayList()
                for (note in backList) {
                    //判断note。getContent（）中是否包含constraint
                    if (note.content != null && note.content!!.contains(constraint)) {
                        list.add(note)
                    }
                }
            }
            results.values = list //将得到的集合保存到FilterResults的values变量中
            results.count = list.size //将集合的大小保存到FilterResults的count变量中
            return results
        }

        //让适配器更新界面
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            noteList = results.values as List<Note>
            if (results.count > 0) {
                notifyDataSetChanged() //通知数据发生了改变            }
            } else {
                notifyDataSetInvalidated() //通知数据失效
            }
        }
    }

    init {
        this.noteList = noteList
        backList = noteList
    }
}