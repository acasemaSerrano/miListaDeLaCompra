package com.acasema.listadelacompra.ui.otherspinglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.Permissions
import com.acasema.listadelacompra.service.FirebaseAuthService
import com.acasema.listadelacompra.service.FirebaseFirestoreService

/**
 * autor: acasema (alfonso)
 *  clase derivada de ViewModel.
 */
class OtherShopingListViewModel: ViewModel() {

    private val elementListLiveData: MutableLiveData<List<Element>> = MutableLiveData()
    private val emailEditing: MutableLiveData<String> = MutableLiveData()
    private var isEditing: Boolean = false

    fun getListLiveData(): LiveData<List<Element>> {
        return elementListLiveData
    }
    fun getEmailEditing(): LiveData<String> {
        return emailEditing
    }

    fun updateList(permissions: Permissions) {

        FirebaseFirestoreService().getOtherDataDocRef(permissions.owner, permissions.shopingList)
            .addSnapshotListener { value, error ->

                val TAG = "___OtherShopingListViewModel___"
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (value != null && value.exists()) {

                    isEditing = (value.data!![FirebaseFirestoreService().EDITINGKEY] as String?) != null

                    val mutableElements: MutableList<Element> = mutableListOf()

                    (value.data!![FirebaseFirestoreService().ELEMENTSKEY] as List<*>).forEach { fElementInHashMap ->
                        val elementInHashMap = fElementInHashMap as HashMap<*, *>
                        mutableElements.add(Element.transformHashMapAnElement(elementInHashMap))
                    }
                    elementListLiveData.postValue(mutableElements)


                    Log.d(TAG, "Current data: ${value.data!![FirebaseFirestoreService().EDITINGKEY]}")
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
    }

    fun retarIsEditing(permissions: Permissions) {

        FirebaseFirestoreService().getOtherData(permissions.owner, permissions.shopingList)
            .addOnSuccessListener {
                if (it[FirebaseFirestoreService().EDITINGKEY] as String? == FirebaseAuthService().getUser().email){
                    FirebaseFirestoreService().setEditingOtherData(permissions.owner, permissions.shopingList, true)
            }
        }

    }

    fun getEmailEditing(permissions: Permissions) {
        FirebaseFirestoreService().getOtherData(permissions.owner, permissions.shopingList)
            .addOnSuccessListener {
                emailEditing.postValue(it[FirebaseFirestoreService().EDITINGKEY] as String?)
        }
    }

    fun setEditing(permissions: Permissions): Boolean {
        return if (!isEditing){
            FirebaseFirestoreService().setEditingOtherData(permissions.owner, permissions.shopingList, false)
            true
        }else
            false
    }
}
