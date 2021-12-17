package com.acasema.listadelacompra.ui.listcreation

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
class ListCreationViewModel : ViewModel()  {

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

    fun updateData(shopingList: ShopingList, elements: List<Element>) {
        viewModelScope.launch{
            if(shopingList.online == true){
                FirebaseFirestoreService().setEditingData(shopingList.name, true)
                FirebaseFirestoreService().setData(shopingList, elements)
            }else{
                RepositoyShopingList.add(shopingList).runCatching {
                    RepositoryElement.addAll(elements)
                }
            }
            resultSalveDataLiveData.postValue(true)
        }
    }

    fun uploadData(shopingList: ShopingList, elements: List<Element>) {
        viewModelScope.launch{
            if(shopingList.online == true){
                FirebaseFirestoreService().getData(shopingList.name).addOnSuccessListener {
                    if(it.data !=null){
                        resultSalveDataLiveData.postValue(false)
                    }
                    else{
                        FirebaseFirestoreService().setEditingData(shopingList.name, true)
                        FirebaseFirestoreService().setData(shopingList, elements)
                        resultSalveDataLiveData.postValue(true)
                    }
                }
            }else{
                if (!RepositoyShopingList.isExists(shopingList)){
                    RepositoyShopingList.add(shopingList).runCatching {
                        RepositoryElement.addAll(elements)

                        resultSalveDataLiveData.postValue(true)
                    }
                }
                else {
                    resultSalveDataLiveData.postValue(false)
                }
            }
        }
    }

    fun delete(shopingList: ShopingList, elements: List<Element>) {
        viewModelScope.launch{
            if(shopingList.online == true){
                FirebaseFirestoreService().getData(shopingList.name).addOnSuccessListener {
                    val userDeletePrermision:  MutableList<String> = mutableListOf()
                    try {
                        (it.get(FirebaseFirestoreService().PERMISSIONKEY) as List<*>).forEach { fPermissionInHashMap ->
                            val permisionInHashMap = fPermissionInHashMap as HashMap<*, *>
                            userDeletePrermision.add(Permissions.transformHashMapAnPermissions(permisionInHashMap).tosomeone)
                        }
                    }catch (e : NullPointerException){ }
                    FirebaseFirestoreService().setTakeOutPermissions(shopingList.name, userDeletePrermision)
                }


                FirebaseFirestoreService().deleteData(shopingList.name)
            }else{
                RepositoryElement.deleted(elements)
                RepositoyShopingList.deleted(shopingList)
            }
        }
    }
}