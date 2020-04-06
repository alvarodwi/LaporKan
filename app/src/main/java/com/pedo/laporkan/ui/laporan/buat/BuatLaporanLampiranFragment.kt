package com.pedo.laporkan.ui.laporan.buat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.pedo.laporkan.R
import com.pedo.laporkan.databinding.FragmentBuatLaporanLampiranBinding
import com.pedo.laporkan.databinding.FragmentBuatLaporanTulisanBinding

/**
 * A simple [Fragment] subclass.
 */
class BuatLaporanLampiranFragment : Fragment() {
    private lateinit var binding: FragmentBuatLaporanLampiranBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(BuatLaporanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuatLaporanLampiranBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val arguments = BuatLaporanLampiranFragmentArgs.fromBundle(arguments!!)
        viewModel.setIncompleteLaporan(arguments.itemLaporan)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isPhotoAttached.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(!it){
                    binding.btnLanjutkan.text = "LEWATI"
                }else{
                    binding.btnLanjutkan.text = "LANJUTKAN"
                }
            }
        })

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let{
                when(it){
                    2 -> {
                        findNavController().navigate(
                            BuatLaporanLampiranFragmentDirections.lampiranToTinjau(viewModel.getIncompleteLaporan())
                        )
                        viewModel.doneNextAction()
                    }
                    else -> {
                        //do nothing
                    }
                }
            }
        })

    }
}
