package com.acasema.listadelacompra.service

import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.data.model.Permissions
import com.acasema.listadelacompra.data.model.ShopingList
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions

class FirebaseFirestoreService {

    private val db = FirebaseFirestore.getInstance()
    private val userEmail = FirebaseAuthService().getUser().email

    private val usersCollection = "users"
    private val dataCollection = "data"
    private val dataPermissionCollection = "dataPermission"

    val UNION = "+"

    val NAMEKEY = "name"
    val ELEMENTSKEY = "elements"
    val EDITINGKEY = "editing"
    val PERMISSIONKEY = "permission"

    fun setProfileData(userName: String) {
        db.collection(usersCollection).document(userEmail!!).set(hashMapOf(NAMEKEY to userName))
    }
    fun getProfileData(): Task<DocumentSnapshot> {
        return db.collection(usersCollection).document(userEmail!!).get()
    }

    fun getAllProfile(): Task<QuerySnapshot> {
        return db.collection(usersCollection).get()
    }


    fun setData(shopingList: ShopingList, elements: List<Element>) {
        db.collection(usersCollection).document(userEmail!!)
            .collection(dataCollection).document(shopingList.name)
            .set(hashMapOf(ELEMENTSKEY to elements), SetOptions.mergeFields(ELEMENTSKEY))

    }

    fun getAllData(): Task<QuerySnapshot> {
        return db.collection(usersCollection).document(userEmail!!)
            .collection(dataCollection).get()
    }

    fun getData(shopingListName: String): Task<DocumentSnapshot> {
        return db.collection(usersCollection).document(userEmail!!)
            .collection(dataCollection).document(shopingListName).get()
    }

    fun setEditingData(shopingList: String, setNull: Boolean ){
        db.collection(usersCollection).document(userEmail!!)
            .collection(dataCollection).document(shopingList)
            .set(mapOf(EDITINGKEY to if(setNull) null else userEmail), SetOptions.mergeFields(EDITINGKEY))
    }

    fun getDataDocRef(shopingList: String): DocumentReference {
        return getOtherDataDocRef(userEmail!!, shopingList)
    }

    fun setListPermissions(shopingList: String, permissions: List<Permissions>) {
        db.collection(usersCollection).document(userEmail!!)
            .collection(dataCollection).document(shopingList)
            .set(hashMapOf(PERMISSIONKEY to permissions), SetOptions.mergeFields(PERMISSIONKEY))
        permissions.forEach {
            setDataPermission(it)
        }
    }

    fun getDataAllPermission(): Task<QuerySnapshot> {
        return db.collection(usersCollection).document(userEmail!!)
            .collection(dataPermissionCollection).get()
    }

    fun setDataPermission(permissions: Permissions): Task<Void> {
        return db.collection(usersCollection).document(permissions.tosomeone)
            .collection(dataPermissionCollection).document(userEmail!! + UNION + permissions.shopingList)
            .set(hashMapOf(PERMISSIONKEY to permissions), SetOptions.mergeFields(PERMISSIONKEY))
    }

    fun getOtherData(owner: String, shopingList: String): Task<DocumentSnapshot> {
        return db.collection(usersCollection).document(owner)
            .collection(dataCollection).document(shopingList).get()
    }

    fun getOtherDataDocRef(owner: String, shopingList: String): DocumentReference {
        return db.collection(usersCollection).document(owner)
            .collection(dataCollection).document(shopingList)
    }

    fun setEditingOtherData(permissions: Permissions, setNull: Boolean) {
        db.collection(usersCollection).document(permissions.owner)
            .collection(dataCollection).document(permissions.shopingList)
            .set(mapOf(EDITINGKEY to if(setNull) null else userEmail), SetOptions.mergeFields(EDITINGKEY))
    }

}