package com.acasema.listadelacompra.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * autor: acasema (alfonso)
 * clase model de Element
 */
@Entity
data class Element(
    @PrimaryKey(autoGenerate = false) var name:String,
    var amount:String,
    var isBought:Boolean = false,
    var lastDatePurchased: String?,
    @NonNull
    var shopingListName:String,
    ){



    companion object{
        fun transformHashMapAnElement(elementInHashMap : HashMap<*, *>) : Element{
            val shopingListName =  elementInHashMap["shopingListName"] as String
            val amount =  elementInHashMap["amount"] as String
            val bought =  elementInHashMap["bought"] as Boolean
            val name =  elementInHashMap["name"] as String
            val lastDatePurchased =  elementInHashMap["lastDatePurchased"] as String?

            return Element(name, amount, bought, lastDatePurchased, shopingListName)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Element

        if (name != other.name) return false
        if (shopingListName != other.shopingListName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + shopingListName.hashCode()
        return result
    }
}
