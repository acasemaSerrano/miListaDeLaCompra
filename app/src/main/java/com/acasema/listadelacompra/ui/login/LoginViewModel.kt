package com.acasema.listadelacompra.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

import com.acasema.listadelacompra.service.FirebaseAuthService
import com.acasema.listadelacompra.service.FirebaseFirestoreService

class LoginViewModel : ViewModel() {

    private val completeInitAuthLiveData: MutableLiveData<Boolean?> = MutableLiveData()
    private val completeInitAuthGoogleLiveData: MutableLiveData<Boolean?> = MutableLiveData()

    private var _email = String()
    private var _password = String()

    fun setEmail(email:String){
        _email = email
    }
    fun setPassword(password:String){
        _password = password
    }
    fun getEmail(): String{
        return _email
    }

    fun completeInitAuthLiveData() : LiveData<Boolean?> {
        return completeInitAuthLiveData
    }

    fun completeInitAuthGoogleLiveData() : LiveData<Boolean?> {
        return completeInitAuthGoogleLiveData
    }

    fun initAuth() {

        val fbService = FirebaseAuthService()

        fbService.login(_email, _password).addOnCompleteListener {
                if (it.isSuccessful)
                    completeInitAuthLiveData.postValue(true)
                else
                    completeInitAuthLiveData.postValue(false)
            }
    }
    fun initAuthGoogle(account: GoogleSignInAccount) {

        val fbService = FirebaseAuthService()

        fbService.loginGoogle(account).addOnCompleteListener {
            if (it.isSuccessful){
                completeInitAuthGoogleLiveData.postValue(true)
                FirebaseFirestoreService().setProfileData(fbService.getUser().displayName!!)
            }
            else
                completeInitAuthGoogleLiveData.postValue(false)
        }
    }

}