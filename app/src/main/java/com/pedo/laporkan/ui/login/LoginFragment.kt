package com.pedo.laporkan.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import com.pedo.laporkan.R
import com.pedo.laporkan.databinding.FragmentLoginBinding
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireNotNull(activity).application)
        ).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
    }

    private fun initViewModel(){
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    AuthenticationState.SUKSES -> {
                        openBeranda()
                    }
                    AuthenticationState.PASSWORD_SALAH -> {
                        Log.d(DEFAULT_TAG,"Wrong Password")
                    }
                    AuthenticationState.USERNAME_SALAH -> {
                        Log.d(DEFAULT_TAG,"Unknown Username")
                    }
                }
            }
        })

        viewModel.toRegisterAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    openRegister()
                    viewModel.doneRegisterAction()
                }
            }
        })

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity?.applicationContext,it,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun openRegister(){
        findNavController().navigate(LoginFragmentDirections.loginToRegister())
    }

    private fun openBeranda(){
        findNavController().navigate(LoginFragmentDirections.loginToBeranda())
    }

}
