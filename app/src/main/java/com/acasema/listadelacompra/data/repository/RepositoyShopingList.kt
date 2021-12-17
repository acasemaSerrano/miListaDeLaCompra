package com.acasema.listadelacompra.data.repository

import com.acasema.listadelacompra.data.AppDatabase
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.ShopingList

/**
 * autor: acasema (alfonso)
 * clase para manejar el dao de ShopingList
 */
class RepositoyShopingList {
    companion object{
        private var shopingListDao = AppDatabase.getInstance().ShopingListDao()

        fun get(): List<ShopingList> {
            return shopingListDao.getAll()
        }

        fun add(shopingList:ShopingList){
            shopingListDao.insert(shopingList)
        }

        fun isExists(shopingList: ShopingList): Boolean {
            return shopingListDao.getCount(shopingList.name) == 1
        }

        fun deleted(shopingList: ShopingList) {
            shopingListDao.delete(shopingList)
        }

    }
}