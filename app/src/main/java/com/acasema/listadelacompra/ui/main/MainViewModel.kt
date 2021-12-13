package com.acasema.listadelacompra.ui.main

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri

import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acasema.listadelacompra.R

import com.acasema.listadelacompra.service.FirebaseAuthService
import com.acasema.listadelacompra.service.FirebaseFirestoreService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.net.URL

class MainViewModel : ViewModel() {

    private val emailLiveData: MutableLiveData<String> = MutableLiveData()
    private val nameLiveData: MutableLiveData<String> = MutableLiveData()
    private val photoUserLiveData: MutableLiveData<Drawable> = MutableLiveData()

    fun emailLiveData() : LiveData<String> {
        return emailLiveData
    }
    fun nameLiveData() : LiveData<String> {
        return nameLiveData
    }
    fun photoUserLiveData() : LiveData<Drawable> {
        return photoUserLiveData
    }

    fun navHeaderInit(resources : Resources) {
        val user = FirebaseAuthService().getUser()
        val fbFirestoreService = FirebaseFirestoreService()

        viewModelScope.launch {

            emailLiveData.postValue(user.email!!)

            fbFirestoreService.getProfileData().addOnSuccessListener {
                val name = it.get(fbFirestoreService.NAMEKEY) as String
                nameLiveData.postValue(name)
            }

            withContext(Dispatchers.Default) {

                //recogemos la uri
                val photoUri: Uri? = user.photoUrl
                val photoUrl: URL
                if (photoUri == null) {
                    photoUserLiveData.postValue(resources.getDrawable(R.drawable.ic_launcher, null))
                }
                else{
                    photoUrl = URL(photoUri.toString())
                    //descargamos la imagen
                    val photo: Bitmap =
                        BitmapFactory.decodeStream(photoUrl.openConnection().getInputStream())
                    //creamos el drawable redondeado
                    val roundedPhoto: RoundedBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(resources, photo)
                    //asignamos el CornerRadius
                    roundedPhoto.cornerRadius = photo.height.toFloat()

                    photoUserLiveData.postValue(roundedPhoto)
                }
            }
        }
    }

    fun signOff() {
        FirebaseAuthService().signOff()
    }
}