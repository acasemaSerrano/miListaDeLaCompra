package com.acasema.listadelacompra.ui.controller

import android.view.View

interface FabController {
    fun setOnClickListener(listener : View.OnClickListener)
    fun setVisibility(isVisibility : Boolean)
    fun setImageResource(resource: Int)
}