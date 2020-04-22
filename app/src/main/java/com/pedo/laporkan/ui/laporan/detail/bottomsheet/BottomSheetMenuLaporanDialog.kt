package com.pedo.laporkan.ui.laporan.detail.bottomsheet

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.bold
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.databinding.FragmentBottomSheetMenuLaporanBinding
import com.pedo.laporkan.ui.laporan.detail.DetailLaporanFragmentDirections
import com.pedo.laporkan.utils.Constants.FilterDaftarLaporan.LAPORAN_BARU
import com.pedo.laporkan.utils.Constants.FilterDaftarLaporan.LAPORAN_PROSES

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
                        showUbahStatusDialog()
                        viewModel.resetNextAction()
                    }
                    3 -> {
                        showTutupLaporanDialog()
                        viewModel.resetNextAction()
                    }
                }
            }
        })
    }

    private fun showUbahStatusDialog(){
        val msg = when(viewModel.getStatusLaporan()){
            LAPORAN_BARU -> SpannableStringBuilder()
                .append("Anda akan mengubah status\nlaporan ini menjadi ")
                .bold { append("DIPROSES") }
                .append(".\nArtinya, laporan ini sudah valid\ndan dapat ditanggapi petugas.")
            LAPORAN_PROSES -> SpannableStringBuilder()
                .append("Anda akan mengubah status\nlaporan ini menjadi ")
                .bold { append("SELESAI") }
                .append(".\nLaporan tidak bisa diubah kembali.")
            else -> ""
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Tindakan")
            .setMessage(msg)
            .setPositiveButton("Oke") { _, _ ->
                viewModel.ubahStatusLaporan()
                this.dismiss()
                Toast.makeText(context,"Status Laporan Diubah!",Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Batal"){ _,_ ->
                //do nothing
            }
            .show()
    }

    private fun showTutupLaporanDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Tindakan")
            .setMessage("Anda akan menutup laporan.\nArtinya laporan ini Anda anggap tidak valid.")
            .setPositiveButton("Oke") { _, _ ->
                viewModel.tutupLaporan()
                this.dismiss()
                Toast.makeText(context,"Laporan Ditutup!",Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Batal"){ _,_ ->
                //do nothing
            }
            .show()
    }

    companion object {
        const val TAG = "MenuLaporanDialog"
    }
}