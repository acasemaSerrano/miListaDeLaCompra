package com.acasema.listadelacompra.ui.controller

import android.view.View

/**
 * autor: acasema (alfonso)
 *  interfaz para controlar el boton flotante
 */
interface FabController {
    fun setOnClickListener(listener : View.OnClickListener)
    fun setVisibility(isVisibility : Boolean)
    fun setImageResource(resource: Int)
}