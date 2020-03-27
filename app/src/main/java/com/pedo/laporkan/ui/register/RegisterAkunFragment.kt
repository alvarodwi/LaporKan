package com.pedo.laporkan.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.pedo.laporkan.R
import com.pedo.laporkan.databinding.FragmentKodeAdminBinding
import com.pedo.laporkan.databinding.FragmentRegisterAkunBinding

/**
 * A simple [Fragment] subclass.
 */
class RegisterAkunFragment : Fragment() {
    private lateinit var binding: FragmentRegisterAkunBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            RegisterViewModel.Factory(requireActivity().application)
        ).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val arguments = RegisterAkunFragmentArgs.fromBundle(arguments!!)
        binding = FragmentRegisterAkunBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.setIncompleteUser(arguments.userData)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tfPassword2.error = viewModel.confirmPasswordErrorText.value

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    2 -> {
                        findNavController().navigate(
                            RegisterAkunFragmentDirections.register2ToLogin()
                        )
                        viewModel.doneNextAction()
                    }
                }
            }
        })

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context,it, Toast.LENGTH_LONG).show()
            }
        })
    }
}
