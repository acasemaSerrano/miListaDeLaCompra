package com.acasema.listadelacompra.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.ShopingList

/**
 * autor: acasema (alfonso)
 *  clase para representar una relacion entre shopingLis y Element que es de 1:N
 */
data class ShoppingListWithElement(
    @Embedded
    val shopingLis: ShopingList,

    @Relation(parentColumn = "name", entityColumn = "shopingListName")
    val elements:List<Element>
    )