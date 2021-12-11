package com.example.note.translate

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.note.R
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

open class TranslateActivity : AppCompatActivity() {
    var requestBtn: Button? = null
    var showText: TextView? = null
    var etWord: EditText? = null

    val okhttpListener = object : EventListener() {

        override fun dnsStart(call: Call, domainName: String) {
            super.dnsStart(call, domainName)

//            showText?.text = showText?.text.toString() + "\nDns Search:" + domainName
        }

        override fun responseBodyStart(call: Call) {
            super.responseBodyStart(call)
//            showText?.text = showText?.text.toString() + "\nResponse Start"
        }
    }
    val client: OkHttpClient = OkHttpClient
        .Builder()
//        .addInterceptor(TimeConsumeInterceptor())
        .eventListener(okhttpListener).build()

    val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        requestBtn = findViewById(R.id.send_request)
        showText = findViewById(R.id.show_text)
        etWord = findViewById(R.id.et_word)

        requestBtn?.setOnClickListener {
            showText?.text = "正在翻译..."
            click()
        }

        val translateWords = intent.getStringExtra("translate_words")
        if (!TextUtils.isEmpty(translateWords)) {
            etWord?.setText(translateWords)
            click()
        }

    }

    fun request(url: String, callback: Callback) {
        val request: Request = Request.Builder()
            .url(url)
            .header("User-Agent", "Sjtu-Android-OKHttp")
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun click() {
        val url = "https://api.66mz8.com/api/translation.php?info=" + etWord?.text.toString().trim()
        request(url, object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                runOnUiThread {
                    showText?.text = e.message
                }

            }

            override fun onResponse(call: Call, response: Response) {
                val bodyString = response.body?.string()

                // 显示翻译过来的详细明细
                runOnUiThread { showText?.text = bodyString }

            }
        })
    }
}