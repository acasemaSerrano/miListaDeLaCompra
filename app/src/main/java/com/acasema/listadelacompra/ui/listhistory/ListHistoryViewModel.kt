package com.acasema.listadelacompra.ui.listhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.repository.RepositoryElement
import com.acasema.listadelacompra.service.FirebaseFirestoreService

class ListHistoryViewModel : ViewModel() {

    private val listLiveData: MutableLiveData<List<Element>> = MutableLiveData()

    fun getListLiveData(): LiveData<List<Element>> {
        return listLiveData
    }

    fun updateList(shopingListName: String, isOnline: Boolean) {
        if(!isOnline)
            listLiveData.postValue(RepositoryElement.getDateIsNotNull(shopingListName))
        else{
            FirebaseFirestoreService().getData(shopingListName)
                .addOnSuccessListener { it ->

                    val mutableElements: MutableList<Element> = mutableListOf()

                    (it.get(FirebaseFirestoreService().ELEMENTSKEY) as List<*>).forEach { fElementInHashMap ->

                        val elementInHashMap = fElementInHashMap as HashMap<*, *>
                        mutableElements.add(Element.transformHashMapAnElement(elementInHashMap))
                    }
                    mutableElements.removeIf { !it.isBought }
                    listLiveData.postValue(mutableElements)
                }
        }
    }
}