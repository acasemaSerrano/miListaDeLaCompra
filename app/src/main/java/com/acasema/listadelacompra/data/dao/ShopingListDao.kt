package com.acasema.listadelacompra.data.dao

import androidx.room.*
import com.acasema.listadelacompra.data.model.ShopingList
/**
 * autor: acasema (alfonso)
 * clase para acceder a los elementos de la tabla ShopingLis
 */
@Dao
interface ShopingListDao {

    @Query("SELECT * FROM shopinglist")
    fun getAll(): List<ShopingList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shopingList: ShopingList)

    @Query("SELECT COUNT(*) FROM shopinglist WHERE name = :name")
    fun getCount(name: String): Int

    @Delete()
    fun delete(shopingList: ShopingList)

}