package com.acasema.listadelacompra.service

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseAuthService {

    private val auth = FirebaseAuth.getInstance()

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }
    fun signIn(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun loginGoogle(account: GoogleSignInAccount): Task<AuthResult> {

        val credential= GoogleAuthProvider.getCredential(account.idToken, null)
        return auth.signInWithCredential(credential)
    }
    fun getUser(): FirebaseUser {
        return auth.currentUser!!
    }
    fun signOff() {
        auth.signOut()
    }
}