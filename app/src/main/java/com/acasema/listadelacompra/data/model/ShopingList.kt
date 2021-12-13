package com.acasema.listadelacompra.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class ShopingList(@PrimaryKey(autoGenerate = false) var name:String) {

    @Ignore
    var online: Boolean? = null
}