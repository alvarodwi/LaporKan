package com.pedo.laporkan.ui.tanggapan.buat

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
import com.pedo.laporkan.databinding.FragmentBuatLaporanTulisanBinding
import com.pedo.laporkan.databinding.FragmentBuatTanggapanBinding
import com.pedo.laporkan.ui.laporan.buat.BuatLaporanViewModel

/**
 * A simple [Fragment] subclass.
 */
class BuatTanggapanFragment : Fragment() {
    private lateinit var binding: FragmentBuatTanggapanBinding
    private lateinit var viewModel: BuatTanggapanViewModel
    private lateinit var bundle : BuatTanggapanFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuatTanggapanBinding.inflate(inflater)
        bundle = BuatTanggapanFragmentArgs.fromBundle(arguments!!)
        viewModel = ViewModelProvider(
            this,
            BuatTanggapanViewModel.Factory(bundle.idLaporan,requireActivity().application)
        ).get(BuatTanggapanViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.appToolbar)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.let {
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    findNavController().navigate(
                        BuatTanggapanFragmentDirections.buatTanggapanToDetailLaporan(bundle.idLaporan)
                    )
                    viewModel.doneNextAction()
                }
            }
        })
    }

}
