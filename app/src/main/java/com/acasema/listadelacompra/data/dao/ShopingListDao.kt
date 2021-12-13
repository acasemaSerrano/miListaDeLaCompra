package com.acasema.listadelacompra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acasema.listadelacompra.data.model.ShopingList

@Dao
interface ShopingListDao {

    @Query("SELECT * FROM shopinglist")
    fun getAll(): List<ShopingList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shopingList: ShopingList)
}