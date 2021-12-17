package com.acasema.listadelacompra.ui.otherlists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acasema.listadelacompra.data.model.Permissions

import com.acasema.listadelacompra.service.FirebaseFirestoreService

/**
 * autor: acasema (alfonso)
 *  clase derivada de ViewModel.
 */
class OtherListsViewModel: ViewModel() {

    private val permissionsListLiveData: MutableLiveData<List<Permissions>> = MutableLiveData()

    fun getListLiveData(): MutableLiveData<List<Permissions>> {
        return permissionsListLiveData
    }

    fun updateList() {
        val permissionsListsMutable :  MutableList<Permissions> = mutableListOf()

        FirebaseFirestoreService().getDataAllPermission().addOnSuccessListener { documents ->
            documents.documents.forEach { value ->

                val permissions = Permissions.transformHashMapAnPermissions(
                    value.data!![FirebaseFirestoreService().PERMISSIONKEY] as HashMap<*,*>)

                permissionsListsMutable.add(permissions)
            }
            permissionsListLiveData.postValue(permissionsListsMutable)
        }
    }
}