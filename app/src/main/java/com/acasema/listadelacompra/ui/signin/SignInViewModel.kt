package com.acasema.listadelacompra.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acasema.listadelacompra.service.FirebaseAuthService
import com.acasema.listadelacompra.service.FirebaseFirestoreService
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {

    private val completeCreateAuthLiveData: MutableLiveData<Boolean?> = MutableLiveData()

    fun getCompleteCreateAuthLiveData() : LiveData<Boolean?> {
        return completeCreateAuthLiveData
    }

    private var _email = String()
    private var _password = String()
    private var _userName = String()

    fun setEmail(email:String){
        _email = email
    }
    fun setPassword(password:String){
        _password = password
    }
    fun setUserName(userName: String) {
        _userName = userName
    }

    fun createAuth() {
        val fbAuthService = FirebaseAuthService()
        val fbFirestoreService = FirebaseFirestoreService()

        fbAuthService.signIn(_email, _password).addOnCompleteListener {
            if (it.isSuccessful){
                completeCreateAuthLiveData.postValue(true)

                viewModelScope.launch {
                    fbFirestoreService.setProfileData(_userName)
                }
            }
            else
                completeCreateAuthLiveData.postValue(false)

        }
    }

}