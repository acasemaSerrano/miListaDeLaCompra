package com.acasema.listadelacompra.ui.listoflist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acasema.listadelacompra.data.model.ShopingList
import com.acasema.listadelacompra.data.repository.RepositoyShopingList
import com.acasema.listadelacompra.service.FirebaseFirestoreService
import kotlinx.coroutines.launch

/**
 * autor: acasema (alfonso)
 *  clase derivada de ViewModel.
 */
class ListOfListViewModel : ViewModel() {

    private val listLiveData: MutableLiveData<List<ShopingList>> = MutableLiveData()

    fun getListLiveData(): MutableLiveData<List<ShopingList>> {
        return listLiveData
    }

    suspend fun updateList() {
        viewModelScope.launch{

            val shopingListsMutable :  MutableList<ShopingList> = RepositoyShopingList.get() as MutableList<ShopingList>
            for (shopingList in shopingListsMutable)
                shopingList.online = false

            FirebaseFirestoreService().getAllData().addOnSuccessListener {

                for ( shopingListData in it.documents ){
                    val shopingList = ShopingList(shopingListData.id)
                    shopingList.online = true
                    shopingListsMutable.add(shopingList)
                }

                listLiveData.postValue(shopingListsMutable)
            }

        }
    }

}