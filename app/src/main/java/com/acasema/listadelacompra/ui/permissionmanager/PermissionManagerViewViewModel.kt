package com.acasema.listadelacompra.ui.permissionmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.acasema.listadelacompra.data.model.Permissions
import com.acasema.listadelacompra.data.model.ShopingList
import com.acasema.listadelacompra.service.FirebaseAuthService
import com.acasema.listadelacompra.service.FirebaseFirestoreService

class PermissionManagerViewViewModel : ViewModel() {

    private val listLiveData: MutableLiveData<List<Permissions>> = MutableLiveData()
    private val email: MutableLiveData<String?> = MutableLiveData()

    fun getListLiveData(): LiveData<List<Permissions>> {
        return listLiveData
    }
    fun getEmail(): LiveData<String?> {
        return email
    }

    fun updateList(shopingList: String, permissions: List<Permissions>) {
        FirebaseFirestoreService().setListPermissions(shopingList, permissions)
    }

    fun getListPermissions(shopingList: String){
        FirebaseFirestoreService().getData(shopingList).addOnSuccessListener {

            val mutablePermision:  MutableList<Permissions> = mutableListOf()

            try {

                (it.get(FirebaseFirestoreService().PERMISSIONKEY) as List<*>).forEach { fPermissionInHashMap ->
                    val permisionInHashMap = fPermissionInHashMap as HashMap<*, *>
                    mutablePermision.add(Permissions.transformHashMapAnPermissions(permisionInHashMap))
                }
            }catch (e : NullPointerException){

            }

            listLiveData.postValue(mutablePermision)
        }

    }

    fun searchEmail(email: String) {
        FirebaseFirestoreService().getAllProfile().addOnSuccessListener {

            for ( userEmail in it.documents ){
                if (userEmail.id == email){
                    this.email.postValue(email)
                    return@addOnSuccessListener
                }
            }
            this.email.postValue(null)
        }
    }

    fun getOwner(): String {
        return FirebaseAuthService().getUser().email!!
    }
}
