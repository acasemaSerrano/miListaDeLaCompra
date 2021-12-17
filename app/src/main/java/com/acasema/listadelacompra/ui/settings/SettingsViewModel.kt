package com.acasema.listadelacompra.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acasema.listadelacompra.service.FirebaseFirestoreService
import kotlinx.coroutines.launch

/**
 * autor: acasema (alfonso)
 *  clase derivada de ViewModel.
 */
class SettingsViewModel : ViewModel() {

    private val userNameLiveData: MutableLiveData<String> = MutableLiveData()

    fun getUserNameLiveData() : LiveData<String> {
        return userNameLiveData
    }

    fun rename(newName: String) {

        userNameLiveData.postValue(newName)
        viewModelScope.launch {
            FirebaseFirestoreService().setProfileData(newName)
        }
    }

    fun getUserName() {

        viewModelScope.launch {
            FirebaseFirestoreService().getProfileData().addOnSuccessListener {
                val name = it.get(FirebaseFirestoreService().NAMEKEY) as String
                userNameLiveData.postValue(name)
            }
        }
    }


}