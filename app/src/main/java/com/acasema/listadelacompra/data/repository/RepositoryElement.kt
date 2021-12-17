package com.acasema.listadelacompra.data.repository

import com.acasema.listadelacompra.data.AppDatabase
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.ShopingList

/**
 * autor: acasema (alfonso)
 * clase para manejar el dao de Element
 */
class RepositoryElement {
    companion object{

        private var elementDao = AppDatabase.getInstance().ElementDao()

        fun get(shopingListName:String): List<Element> {
            return elementDao.getAll(shopingListName)
        }

        fun getIsBought(shopingListName:String, isBought: Boolean): List<Element> {
            return elementDao.getAllIsBought(shopingListName, isBought)
        }

        fun add(element: Element){
            elementDao.insert(listOf(element))
        }
        fun addAll(elements: List<Element>){
            deleted(get(elements[0].shopingListName))
            elementDao.insert(elements)
        }

        fun getDateIsNotNull(shopingListName:String): List<Element> {
            return elementDao.getDateIsNotNull(shopingListName)
        }

        fun deleted(elements: List<Element>) {
            elementDao.delete(elements)
        }


    }
}