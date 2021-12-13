package com.acasema.listadelacompra.data.repository

import com.acasema.listadelacompra.data.AppDatabase
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.ShopingList

class RepositoyShopingList {
    companion object{

        private var shopingListDao = AppDatabase.getInstance().ShopingListDao()

        suspend fun get(): List<ShopingList> {

           //init()

            return shopingListDao.getAll()
        }

        suspend fun add(shopingList:ShopingList){
            shopingListDao.insert(shopingList)
        }


        private suspend fun init() {
            val shopingList = ShopingList("mercadona")
            shopingList.online = false
            add(shopingList)

            val elements: List<Element> =
                listOf(
                    Element("huevos", "12", false,null, shopingList.name),
                    Element("bananas", "12", false, null, shopingList.name)
                )
            RepositoryElement.addAll(elements)
        }



    }
}