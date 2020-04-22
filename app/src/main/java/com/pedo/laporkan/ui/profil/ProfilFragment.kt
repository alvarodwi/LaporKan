package com.pedo.laporkan.ui.profil

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pedo.laporkan.R
import com.pedo.laporkan.databinding.FragmentProfilBinding

/**
 * A simple [Fragment] subclass.
 */
class ProfilFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireNotNull(activity).application)
        ).get(ProfilViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.appToolbar)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.title = ""

        viewModel.toLoginAfterLogout.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        ProfilFragmentDirections.profilToLogin()
                    )
                    viewModel.doneLogout()
                }
            }
        })

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profil,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                activity?.onBackPressed()
            }
            R.id.edit_profile -> {
                findNavController().navigate(
                    ProfilFragmentDirections.profilToUbahProfil()
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
