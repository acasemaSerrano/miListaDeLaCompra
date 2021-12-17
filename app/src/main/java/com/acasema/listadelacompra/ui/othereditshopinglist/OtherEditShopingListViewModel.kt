package com.acasema.listadelacompra.ui.othereditshopinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.Permissions
import com.acasema.listadelacompra.data.model.ShopingList
import com.acasema.listadelacompra.data.repository.RepositoryElement
import com.acasema.listadelacompra.data.repository.RepositoyShopingList
import com.acasema.listadelacompra.service.BarcodeInterpreterService
import com.acasema.listadelacompra.service.FirebaseFirestoreService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * autor: acasema (alfonso)
 *  clase derivada de ViewModel.
 */
class OtherEditShopingListViewModel : ViewModel()  {

    private var shopingListNameLiveData: MutableLiveData<String> = MutableLiveData()
    private var listElementLiveData: MutableLiveData<List<Element>> = MutableLiveData()
    private var resultReadBarcodeLiveData: MutableLiveData<String> = MutableLiveData()
    private var resultSalveDataLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getShopingListNameLiveData() : LiveData<String> {
        return shopingListNameLiveData
    }
    fun getListElementLiveData() : LiveData<List<Element>> {
        return listElementLiveData
    }
    fun getResultReadBarcodeLiveData(): LiveData<String> {
        return resultReadBarcodeLiveData
    }
    fun getResultSalveDataLiveData(): LiveData<Boolean> {
        return resultSalveDataLiveData
    }

    fun shopingListNamePost(value: String){
        shopingListNameLiveData.postValue(value)
    }
    fun resultReadCodePost(value: String){
        viewModelScope.launch(Dispatchers.IO) {
            resultReadBarcodeLiveData.postValue(BarcodeInterpreterService.itemRepresenting(value))
        }
    }

    fun getElementList(permissions: Permissions) {
        FirebaseFirestoreService().getOtherData(permissions.owner, permissions.shopingList)
            .addOnSuccessListener {

                val mutableElements: MutableList<Element> = mutableListOf()

                (it.get(FirebaseFirestoreService().ELEMENTSKEY) as List<*>).forEach { fElementInHashMap ->

                    val elementInHashMap = fElementInHashMap as HashMap<*, *>
                    mutableElements.add(Element.transformHashMapAnElement(elementInHashMap))
                }
                listElementLiveData.postValue(mutableElements)
            }
    }

    fun updateData(permission: Permissions, elements: List<Element>) {
        viewModelScope.launch{
            FirebaseFirestoreService().setEditingOtherData(permission.owner, permission.shopingList, true)
            FirebaseFirestoreService().setOtherData(permission.owner, permission.shopingList, elements)
            resultSalveDataLiveData.postValue(true)
        }
    }

    fun delete(shopingList: ShopingList, elements: List<Element>) {
        viewModelScope.launch{
            if(shopingList.online == true){
                FirebaseFirestoreService().deleteData(shopingList.name)
            }else{
                RepositoryElement.deleted(elements)
                RepositoyShopingList.deleted(shopingList)
            }
        }
    }
}