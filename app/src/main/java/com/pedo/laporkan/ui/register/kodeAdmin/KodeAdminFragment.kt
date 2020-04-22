package com.pedo.laporkan.ui.register.kodeAdmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pedo.laporkan.databinding.FragmentKodeAdminBinding
import com.pedo.laporkan.ui.register.RegisterViewModel

/**
 * A simple [Fragment] subclass.
 */
class KodeAdminFragment : Fragment() {
    private lateinit var binding: FragmentKodeAdminBinding
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
        binding = FragmentKodeAdminBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    3 -> {
                        Toast.makeText(context,"Kode Admin sesuai!",Toast.LENGTH_LONG).show()
                        findNavController().navigate(
                            KodeAdminFragmentDirections.kodeAdminToRegister().setUserRole("ADMIN")
                        )
                    }
                }
            }
        })

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }
        })
    }
}
