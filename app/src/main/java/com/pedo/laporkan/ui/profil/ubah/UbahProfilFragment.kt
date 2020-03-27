package com.pedo.laporkan.ui.profil.ubah

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.pedo.laporkan.R
import com.pedo.laporkan.databinding.FragmentProfilBinding
import com.pedo.laporkan.databinding.FragmentUbahProfilBinding
import com.pedo.laporkan.ui.profil.ProfilFragmentDirections
import com.pedo.laporkan.ui.profil.ProfilViewModel

/**
 * A simple [Fragment] subclass.
 */
class UbahProfilFragment : Fragment() {
    private lateinit var binding: FragmentUbahProfilBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireNotNull(activity).application)
        ).get(UbahProfilViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUbahProfilBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.appToolbar)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.let{
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
        }

        viewModel.existingUserData.observe(viewLifecycleOwner, Observer {
            viewModel.showExistingUserData()
        })

        viewModel.toProfil.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        UbahProfilFragmentDirections.ubahProfilToProfil()
                    )
                    viewModel.doneUbahProfil()
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
