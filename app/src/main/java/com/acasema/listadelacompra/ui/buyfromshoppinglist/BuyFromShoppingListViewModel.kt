package com.acasema.listadelacompra.ui.buyfromshoppinglist

import androidx.lifecycle.*
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.ShopingList
import com.acasema.listadelacompra.data.repository.RepositoryElement
import com.acasema.listadelacompra.service.FirebaseFirestoreService
import kotlinx.coroutines.launch

class BuyFromShoppingListViewModel : ViewModel() {


    private val listLiveData: MutableLiveData<List<Element>> = MutableLiveData()
    private var data: MutableList<Element> = mutableListOf()
    var isOnline: Boolean = false

    fun getListLiveData(): LiveData<List<Element>> {
        return listLiveData
    }

    fun updateList(listName: String) {
        if (!isOnline) {
            viewModelScope.launch {
                listLiveData.postValue(RepositoryElement.getIsBought(listName, false))
            }
        } else {
            FirebaseFirestoreService().getData(listName)
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
    }

    fun setBought(element: Element){
        if (!isOnline){
            //se usa add por que puede rescribir
            viewModelScope.launch {
                RepositoryElement.add(element)
            }
        }else{
            data.forEach{dataElement ->
                if (dataElement.equals(element)){
                    data[data.indexOf(dataElement)].isBought = element.isBought
                    data[data.indexOf(dataElement)].lastDatePurchased = element.lastDatePurchased
                }
            }

            FirebaseFirestoreService().setData(ShopingList(element.shopingListName), data)
        }
    }

}