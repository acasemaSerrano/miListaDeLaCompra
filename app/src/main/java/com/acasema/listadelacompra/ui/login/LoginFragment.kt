package com.acasema.listadelacompra.ui.login

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController

import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.databinding.FragmentLoginBinding
import com.acasema.listadelacompra.ui.main.MainActivity
import com.acasema.listadelacompra.ui.controller.ActionBarController
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.utils.Pattern
import com.acasema.listadelacompra.utils.providerType

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class LoginFragment : Fragment() {

    lateinit var fabController: FabController

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    //region ciclo de vida del fragment
    override fun onResume() {
        super.onResume()
        val fab: FabController = (activity as MainActivity).fabController
        fab.setVisibility(false)
        val actionBar: ActionBarController = (activity as MainActivity).actionBarController
        actionBar.disable()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session()

        setOnClickListener()

        liveDataObserve()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    //region iniciación del fragment
    private fun session(){
        val prefs = requireActivity()
            .getSharedPreferences(getString(R.string.prefs_file_key), Context.MODE_PRIVATE)
        val email: String? = prefs.getString("email", null)
        if (email != null){
            requireView().findNavController().navigate(R.id.action_loginFragment_to_listOfListFragment)
        }
    }

    private fun liveDataObserve() {
        loginViewModel.completeInitAuthLiveData().observe(viewLifecycleOwner, {
            if (it!!)
                initAuth(providerType.BASIC)
            else
                showError(getString(R.string.error_initAuth))
        })
        loginViewModel.completeInitAuthGoogleLiveData().observe(viewLifecycleOwner, {
            if (it!!)
                initAuth(providerType.GOOGLE)
            else
                showError(getString(R.string.error_initAuthGoogle))
        })
    }

    private fun setOnClickListener() {
        binding.btnGoogle.setOnClickListener {
            googleSignIn()
        }
        binding.btnSignIn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.signInFragment)
        }
        binding.btnLogin.setOnClickListener {
            if (checkEmail() && checkPassword())
                loginViewModel.initAuth()
        }
    }
    //endregion

    private fun checkEmail(): Boolean {
        return if(!Pattern.isEmailValid(binding.tieEmail.text.toString())) {
            binding.tieEmail.error = getString(R.string.error_EmailValidate)
            false
        } else {
            loginViewModel.setEmail(binding.tieEmail.text.toString())
            true
        }
    }

    private fun checkPassword(): Boolean {
        return if(!Pattern.isPasswordValid(binding.tiePassword.text.toString())) {
            binding.tiePassword.error = getString(R.string.error_PasswordValidate)
            false
        } else{
            loginViewModel.setPassword(binding.tiePassword.text.toString())
            true
        }


    }

    private fun showError(error: String) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.information))
            .setMessage(error)
            .setPositiveButton("cerrar", null)
            .create().show()
    }

    //region iniciar cuenta de diferentes tipos
    private fun initAuth(type: providerType) {

        val prefs = requireActivity()
            .getSharedPreferences(getString(R.string.prefs_file_key), Context.MODE_PRIVATE)
            .edit()
        prefs.putString("email", loginViewModel.getEmail())
        prefs.putString("type", type.toString())
        prefs.apply()
        Navigation.findNavController(requireView()).navigate(R.id.listOfListFragment)
    }

    private fun googleSignIn() {
        val googleConf = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(requireContext(), googleConf)

        googleClient.signOut()

        googleSignIn.launch(googleClient.signInIntent)

    }

    private val googleSignIn = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)!!
            loginViewModel.initAuthGoogle(account)
        }catch (e : ApiException){
            showError( e.message + e.status)
        }//funciona mal

    }
    //endregion
}