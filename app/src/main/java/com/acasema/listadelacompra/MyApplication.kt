package com.acasema.listadelacompra

import android.app.Application
import com.acasema.listadelacompra.data.AppDatabase

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.create(this)
    }
}