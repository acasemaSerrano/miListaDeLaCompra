package com.acasema.listadelacompra.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.acasema.listadelacompra.data.dao.ElementDao
import com.acasema.listadelacompra.data.dao.ShopingListDao
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.ShopingList

/**
 * autor: acasema (alfonso)
 *  clase creadora de la base de datos de room
 */
@Database(entities = [ShopingList::class, Element::class], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun ShopingListDao(): ShopingListDao
    abstract fun ElementDao(): ElementDao

    companion object{

        private  var database: AppDatabase? = null

        fun create(context: Context){
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "database-app"
            ).allowMainThreadQueries().fallbackToDestructiveMigrationOnDowngrade().build()
        }

        fun getInstance(): AppDatabase{
            return database!!
        }
    }
}