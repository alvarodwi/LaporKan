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
import com.pedo.laporkan.databinding.FragmentBuatLaporanTinjauBinding

/**
 * A simple [Fragment] subclass.
 */
class BuatLaporanTinjauFragment : Fragment() {
    private lateinit var binding: FragmentBuatLaporanTinjauBinding
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
        binding = FragmentBuatLaporanTinjauBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val arguments = BuatLaporanTinjauFragmentArgs.fromBundle(arguments!!)
        viewModel.setIncompleteLaporan(arguments.itemLaporan)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let{
                when(it){
                    3 -> {
                        findNavController().navigate(
                            BuatLaporanTinjauFragmentDirections.buatLaporanTinjauToBeranda()
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
