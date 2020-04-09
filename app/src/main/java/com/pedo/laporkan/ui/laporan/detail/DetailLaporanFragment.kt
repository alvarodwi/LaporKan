package com.pedo.laporkan.ui.laporan.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.pedo.laporkan.databinding.FragmentDetailLaporanBinding
import com.pedo.laporkan.ui.laporan.detail.bottomsheet.BottomSheetMenuLaporanDialog
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import com.pedo.laporkan.utils.Constants.FilterDaftarLaporan.LAPORAN_GAGAL
import com.pedo.laporkan.utils.Constants.FilterDaftarLaporan.LAPORAN_SELESAI
import com.pedo.laporkan.utils.Helpers.laporKanDateFormat
import com.pedo.laporkan.utils.Helpers.shortenName

/**
 * A simple [Fragment] subclass.
 */
class DetailLaporanFragment : Fragment() {
    private lateinit var binding: FragmentDetailLaporanBinding
    private lateinit var viewModel: DetailLaporanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailLaporanBinding.inflate(inflater)
        val arguments = DetailLaporanFragmentArgs.fromBundle(arguments!!)

        viewModel = ViewModelProvider(
            this,
            DetailLaporanViewModel.Factory(
                arguments.idLaporan,
                requireActivity().application
            )
        ).get(DetailLaporanViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.appToolbar)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.title = ""

        viewModel.itemLaporan.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d(DEFAULT_TAG, it.toString())
                binding.tvJudul.text = it.judul
                binding.tvIsi.text = it.isi
                binding.tvTanggal.text = laporKanDateFormat.format(it.tanggal)
                binding.tvStatus.text = it.convertStatus()

                when(it.convertStatus()){
                    LAPORAN_SELESAI,LAPORAN_GAGAL -> viewModel.disableMenuDetail()
                }
            }
        })

        viewModel.itemUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvPembuat.text = "Dibuat oleh ${shortenName(it.nama)}"
            }
        })

        viewModel.listTanggapan.observe(viewLifecycleOwner, Observer {
            it?.let {
                val adapter = TanggapanRVAdapter()
                adapter.submitList(it)

                binding.rvTanggapan.layoutManager = LinearLayoutManager(context)
                binding.rvTanggapan.adapter = adapter
            }
        })

        viewModel.menuLaporanArgs.observe(viewLifecycleOwner, Observer {
            it?.let {
                val menuLaporanDialog = BottomSheetMenuLaporanDialog(it)
                menuLaporanDialog.show(
                    childFragmentManager,
                    BottomSheetMenuLaporanDialog.TAG
                )
                viewModel.doneMenuBtnClicked()
            }
        })

        viewModel.showMenuDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.btnContainer.visibility = if(it) View.VISIBLE else View.GONE
            }
        })
    }

}
