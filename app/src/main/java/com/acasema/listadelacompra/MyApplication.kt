package com.acasema.listadelacompra

import android.app.Application
import com.acasema.listadelacompra.data.AppDatabase

/**
 * se tiene que usar esta clase para poder arrancar la base de datos desde el inicio
 * CUIDADO: hay que poner android:name=".MyApplication" en el manifest
 */
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.create(this)
    }
}