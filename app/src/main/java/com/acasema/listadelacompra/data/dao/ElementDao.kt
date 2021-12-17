package com.acasema.listadelacompra.data.dao

import androidx.room.*
import com.acasema.listadelacompra.data.model.Element

/**
 * autor: acasema (alfonso)
 * clase para acceder a los elementos de la tabla Element
 */
@Dao
interface ElementDao {

    @Query("SELECT * FROM element WHERE shopingListName = :shopingListName")
    fun getAll(shopingListName: String): List<Element>

    @Query("SELECT * FROM element WHERE shopingListName = :shopingListName and isBought = :isBought ")
    fun getAllIsBought(shopingListName: String, isBought: Boolean): List<Element>

    @Query("SELECT * FROM element WHERE shopingListName = :shopingListName and lastDatePurchased is not NULL ")
    fun getDateIsNotNull(shopingListName: String): List<Element>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(elements: List<Element>)

    @Delete
    fun delete(elements: List<Element>)

}