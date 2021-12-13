package com.acasema.listadelacompra.utils

import java.io.IOException
import java.lang.RuntimeException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

class Miscellany {
    companion object{
        fun netIsAvailable(): Boolean {
            return try {
                val url = URL("http://www.google.com")
                val conn: URLConnection = url.openConnection()
                conn.connect()
                conn.getInputStream().close()
                true
            } catch (e: MalformedURLException) {
                throw RuntimeException(e)
            } catch (e: IOException) {
                false
            }
        }
    }
}