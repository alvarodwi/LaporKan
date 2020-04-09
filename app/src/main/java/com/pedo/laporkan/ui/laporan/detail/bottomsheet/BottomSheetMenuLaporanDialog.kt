package com.pedo.laporkan.ui.laporan.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.databinding.FragmentBottomSheetMenuLaporanBinding
import com.pedo.laporkan.ui.laporan.detail.DetailLaporanFragmentDirections

class BottomSheetMenuLaporanDialog(val itemLaporan : Laporan) : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentBottomSheetMenuLaporanBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            BottomSheetMenuLaporanViewModel.Factory(
                this.itemLaporan,
                requireActivity().application
            )
        ).get(BottomSheetMenuLaporanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetMenuLaporanBinding.inflate(inflater)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.hideBuatTanggapan.observe(viewLifecycleOwner, Observer {
            it?.let{
                binding.item1.visibility = if(it) View.GONE else View.VISIBLE
            }
        })

        viewModel.hideUbahStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.item2.visibility = if(it) View.GONE else View.VISIBLE
            }
        })

        viewModel.showTutupLaporan.observe(viewLifecycleOwner, Observer {
            it?.let{
                binding.item3.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    1 -> {
                        findNavController().navigate(
                            DetailLaporanFragmentDirections.detailLaporanToBuatTanggapan(itemLaporan.id)
                        )
                        this.dismiss()
                        viewModel.resetNextAction()
                    }
                    2 -> {
                        viewModel.ubahStatusLaporan()
                        this.dismiss()
                        Toast.makeText(context,"Ubah Status!",Toast.LENGTH_LONG).show()
                        viewModel.resetNextAction()
                    }
                    3 -> {
                        viewModel.tutupLaporan()
                        this.dismiss()
                        Toast.makeText(context,"Tutup Laporan!",Toast.LENGTH_LONG).show()
                        viewModel.resetNextAction()
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "MenuLaporanDialog"
    }
}