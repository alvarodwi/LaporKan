package com.pedo.laporkan.ui.beranda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.pedo.laporkan.R
import com.pedo.laporkan.databinding.FragmentBerandaBinding
import com.pedo.laporkan.databinding.FragmentLoginBinding
import com.pedo.laporkan.ui.login.LoginViewModel

/**
 * A simple [Fragment] subclass.
 */
class BerandaFragment : Fragment() {
    private lateinit var binding: FragmentBerandaBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireNotNull(activity).application)
        ).get(BerandaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBerandaBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.toProfil.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        BerandaFragmentDirections.berandaToProfil()
                    )
                    viewModel.doneToProfil()
                }
            }
        })

        viewModel.berandaRole.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it == "Masyarakat"){
                    binding.imgMidLeftCard.setImageResource(R.drawable.ic_add)
                    binding.tvMidLeftCard.text = "Buat Laporan"

                    binding.tvStatistikLeft.text = "Laporan"
                    binding.tvStatistikRight.text = "Ditanggapi"
                }else {
                    binding.imgMidLeftCard.setImageResource(R.drawable.ic_insert_drive_file)
                    binding.tvMidLeftCard.text = "Buat Report"
                    if(it == "Petugas"){
                        binding.tvStatistikLeft.text = "Tanggapan"
                    }else{
                        binding.tvStatistikLeft.text = "Report"
                        binding.tvStatistikRight.text = "Ditanggapi"
                    }
                }
            }
        })

        viewModel.toBuatLaporan.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        BerandaFragmentDirections.berandaToBuatLaporan()
                    )
                    viewModel.doneNavigating()
                }
            }
        })

        viewModel.toListLaporan.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        BerandaFragmentDirections.berandaToDaftarLaporan()
                    )
                    viewModel.doneNavigating()
                }
            }
        })

        viewModel.toReport.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        BerandaFragmentDirections.berandaToDaftarReport()
                    )
                    viewModel.doneNavigating()
                }
            }
        })
    }
}
