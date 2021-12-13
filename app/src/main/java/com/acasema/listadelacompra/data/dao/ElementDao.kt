package com.acasema.listadelacompra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acasema.listadelacompra.data.model.Element

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

}