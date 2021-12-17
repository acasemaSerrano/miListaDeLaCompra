package com.acasema.listadelacompra.utils

import java.lang.Exception

/**
 * autor: acasema (alfonso)
 *  clase que almacena toda función comun
 */
class Miscellany {
    companion object{

        fun isOnlineNet(): Boolean {
            try {
                val p: Process = Runtime.getRuntime().exec("ping -c 1 www.google.es")
                return (p.waitFor() == 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }
}