package com.example.note

import android.app.Application
import com.example.note.wp.WpModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(appModel)
        }
    }

    val appModel = module {
        single { WpModel(applicationContext) }
    }
}