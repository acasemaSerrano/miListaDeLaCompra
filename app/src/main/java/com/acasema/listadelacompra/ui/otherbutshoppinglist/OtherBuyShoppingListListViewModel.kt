package com.acasema.listadelacompra.ui.otherbutshoppinglist

import androidx.lifecycle.*
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.Permissions
import com.acasema.listadelacompra.service.FirebaseFirestoreService
import com.acasema.listadelacompra.ui.contract.BuyViewModelContract

/**
 * autor: acasema (alfonso)
 *  clase derivada de ViewModel.
 */
class OtherBuyShoppingListListViewModel : ViewModel(), BuyViewModelContract {

    private val listLiveData: MutableLiveData<List<Element>> = MutableLiveData()
    private var data: MutableList<Element> = mutableListOf()
    private lateinit var permission: Permissions

    fun getListLiveData(): LiveData<List<Element>> {
        return listLiveData
    }

    fun updateList(permission: Permissions) {
        this.permission = permission
        FirebaseFirestoreService().getOtherData(permission.owner,permission.shopingList)
            .addOnSuccessListener { it ->

                val mutableElements:  MutableList<Element> = mutableListOf()
                (it.get(FirebaseFirestoreService().ELEMENTSKEY) as List<*>).forEach { fElementInHashMap ->
                    val elementInHashMap = fElementInHashMap as HashMap<*, *>
                    mutableElements.add(Element.transformHashMapAnElement(elementInHashMap))
                    data.add(Element.transformHashMapAnElement(elementInHashMap))
                }
                mutableElements.removeIf { it.isBought }
                listLiveData.postValue(mutableElements)
        }
    }



    override fun setBought(element: Element){
        data.forEach{dataElement ->
            if (dataElement.equals(element)){
                data[data.indexOf(dataElement)].isBought = element.isBought
                data[data.indexOf(dataElement)].lastDatePurchased = element.lastDatePurchased
            }
        }
        FirebaseFirestoreService().setOtherData(permission.owner, permission.shopingList, data)
    }

    fun cancel(permission: Permissions){
        FirebaseFirestoreService().setEditingOtherData(permission.owner, permission.shopingList, true)
    }
}