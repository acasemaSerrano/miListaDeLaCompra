package com.acasema.listadelacompra.ui.signin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.databinding.FragmentSigninBinding
import com.acasema.listadelacompra.ui.main.MainActivity
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.utils.Pattern

/**
 * autor: acasema (alfonso)
 *  clase derivada de fragment: sirve para registrarse
 */
class SignInFragment: Fragment() {
    private lateinit var signInViewModel: SignInViewModel
    private var _binding: FragmentSigninBinding? = null

    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        val a: FabController = (activity as MainActivity).fabController
        a.setVisibility(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]

        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener{
            if (checkEmail() && checkPassword() && checkUserName())
                signInViewModel.createAuth()
        }

        signInViewModel.getCompleteCreateAuthLiveData().observe(viewLifecycleOwner, {
            if(it!!)
                createAuth()
            else
                showError(getString(R.string.error_initAuth))
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkEmail(): Boolean {
        return if(!Pattern.isEmailValid(binding.tieEmail.text.toString())) {
            binding.tieEmail.error = getString(R.string.error_EmailValidate)
            false
        } else{
            signInViewModel.setEmail(binding.tieEmail.text.toString())
            true
        }
    }

    private fun checkPassword(): Boolean {
        return if(!Pattern.isPasswordValid(binding.tiePassword.text.toString())) {
            binding.tiePassword.error = getString(R.string.error_PasswordValidate)
            false
        } else{
            signInViewModel.setPassword(binding.tiePassword.text.toString())
            true
        }
    }

    private fun checkUserName(): Boolean {
        return if(!Pattern.isText3oMasValid(binding.tieUserName.text.toString())) {
            binding.tieUserName.error = getString(R.string.error_UserNameValidate)
            false
        } else{
            signInViewModel.setUserName(binding.tieUserName.text.toString())
            true
        }
    }
    private fun createAuth() {
        requireView().findNavController().navigate(R.id.action_signInFragment_to_listOfListFragment)
    }

    private fun showError(error: String) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.information))
            .setMessage(error)
            .setPositiveButton("cerrar", null)
            .create().show()
    }

}