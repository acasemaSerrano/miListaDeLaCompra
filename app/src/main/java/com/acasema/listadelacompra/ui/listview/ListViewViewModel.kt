package com.acasema.listadelacompra.ui.listview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.repository.RepositoryElement
import com.acasema.listadelacompra.service.FirebaseAuthService
import com.acasema.listadelacompra.service.FirebaseFirestoreService
import kotlinx.coroutines.launch

/**
 * autor: acasema (alfonso)
 *  clase derivada de ViewModel.
 */
class ListViewViewModel : ViewModel() {
    private val listLiveData: MutableLiveData<List<Element>> = MutableLiveData()
    private val emailEditing: MutableLiveData<String> = MutableLiveData()
    var isOnline: Boolean = false
    var isEditing: Boolean = false

    fun getListLiveData(): LiveData<List<Element>> {
        return listLiveData
    }
    fun getEmailEditingLiveData(): LiveData<String> {
        return emailEditing
    }

    fun updateList(listName: String) {
        if (!isOnline) {
            viewModelScope.launch {
                listLiveData.postValue(RepositoryElement.get(listName))
            }
        } else {
            EditingDataDocRe(listName)
        }
    }

    fun setEditing(listName: String) : Boolean {
        return if (!isEditing){
            FirebaseFirestoreService().setEditingData(listName, false)
            true
        }else
            false
    }

    private fun EditingDataDocRe(listName: String){
        FirebaseFirestoreService().getDataDocRef(listName).addSnapshotListener { value, error ->
            val TAG = "___ListViewViewModel___"
            if (error != null) {
                Log.w(TAG, "Listen failed.", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {

                isEditing = (value.data!![FirebaseFirestoreService().EDITINGKEY] as String?) != null

                val mutableElements:  MutableList<Element> = mutableListOf()
                (value.data!![FirebaseFirestoreService().ELEMENTSKEY] as List<*>).forEach { fElementInHashMap ->
                    val elementInHashMap = fElementInHashMap as HashMap<*, *>
                    mutableElements.add(Element.transformHashMapAnElement(elementInHashMap))
                }
                listLiveData.postValue(mutableElements)

                Log.d(TAG, "Current data: ${value.data!![FirebaseFirestoreService().EDITINGKEY]}")
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    fun retarIsEditing(listName: String) {

        FirebaseFirestoreService().getData(listName).addOnSuccessListener {
            if (it[FirebaseFirestoreService().EDITINGKEY] as String? == FirebaseAuthService().getUser().email){
                FirebaseFirestoreService().setEditingData(listName, true)
            }
        }

    }

    fun getEmailEditing(listName: String) {
        FirebaseFirestoreService().getData(listName).addOnSuccessListener {
            emailEditing.postValue(it[FirebaseFirestoreService().EDITINGKEY] as String?)
        }
    }
}