package com.acasema.listadelacompra.ui.listcreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.ShopingList
import com.acasema.listadelacompra.data.repository.RepositoryElement
import com.acasema.listadelacompra.data.repository.RepositoyShopingList
import com.acasema.listadelacompra.service.BarcodeInterpreterService
import com.acasema.listadelacompra.service.FirebaseFirestoreService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListCreationViewModel : ViewModel()  {

    private var shopingListNameLiveData: MutableLiveData<String> = MutableLiveData()
    private var listElementLiveData: MutableLiveData<List<Element>> = MutableLiveData()
    private var resultReadBarcodeLiveData: MutableLiveData<String> = MutableLiveData()

    fun getShopingListNameLiveData() : LiveData<String> {
        return shopingListNameLiveData
    }
    fun getListElementLiveData() : LiveData<List<Element>> {
        return listElementLiveData
    }
    fun getResultReadBarcodeLiveData(): LiveData<String> {
        return resultReadBarcodeLiveData
    }

    fun shopingListNamePost(value: String){
        shopingListNameLiveData.postValue(value)
    }
    fun resultReadCodePost(value: String){
        viewModelScope.launch(Dispatchers.IO) {
            resultReadBarcodeLiveData.postValue(BarcodeInterpreterService.itemRepresenting(value))
        }
    }


    fun uploadData(shopingList: ShopingList, elements: List<Element>) {
        viewModelScope.launch{
            if(shopingList.online == true){
                FirebaseFirestoreService().setEditingData(shopingList.name, true)
                FirebaseFirestoreService().setData(shopingList, elements)
            }else{
                RepositoyShopingList.add(shopingList).runCatching {
                    RepositoryElement.addAll(elements)
                }
            }
        }
    }

    fun getElementList(shopingListName: String, isOnline: Boolean) {
        if (!isOnline)
            listElementLiveData.postValue(RepositoryElement.get(shopingListName))
        else {
            FirebaseFirestoreService().getData(shopingListName)
                .addOnSuccessListener {

                    val mutableElements: MutableList<Element> = mutableListOf()

                    (it.get(FirebaseFirestoreService().ELEMENTSKEY) as List<*>).forEach { fElementInHashMap ->

                        val elementInHashMap = fElementInHashMap as HashMap<*, *>
                        mutableElements.add(Element.transformHashMapAnElement(elementInHashMap))
                    }
                    listElementLiveData.postValue(mutableElements)
                }
        }
    }

}