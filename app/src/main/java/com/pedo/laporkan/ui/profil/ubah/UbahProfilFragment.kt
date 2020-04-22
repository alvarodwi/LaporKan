package com.pedo.laporkan.ui.profil.ubah

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pedo.laporkan.databinding.FragmentUbahProfilBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                showCancelUbahProfilDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showCancelUbahProfilDialog(){
        val msg = SpannableStringBuilder()
            .append("Anda akan meninggalkan laman\n")
            .bold { append("Ubah Profil") }
            .append(". Perubahan profil\nyang sedang Anda buat akan hilang")

        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Tindakan")
            .setMessage(msg)
            .setPositiveButton("Ya") { _, _ ->
                findNavController().navigateUp()
            }
            .setNegativeButton("Tidak"){ _,_ ->
                //do nothing
            }
            .show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
