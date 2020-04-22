package com.pedo.laporkan.ui.laporan.listing.template

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedo.laporkan.databinding.FragmentDaftarLaporanBaseBinding
import com.pedo.laporkan.ui.laporan.listing.DaftarLaporanFragmentDirections
import com.pedo.laporkan.ui.laporan.listing.DaftarLaporanRVAdapter
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG


abstract class DaftarLaporanBaseFragment(private val criteria: String) : Fragment() {
    private lateinit var binding: FragmentDaftarLaporanBaseBinding
    private lateinit var viewModel: DaftarLaporanViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaftarLaporanBaseBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(
            this,
            DaftarLaporanViewModel.Factory(criteria, requireActivity().application)
        ).get(DaftarLaporanViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DaftarLaporanRVAdapter(
            DaftarLaporanRVAdapter.OnClickListener{
                viewModel.displayLaporanDetail(it)
            }
        )

        binding.rvLaporanListing.layoutManager = LinearLayoutManager(context)
        binding.rvLaporanListing.adapter = adapter

        viewModel.toDetail.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(
                    DaftarLaporanFragmentDirections.daftarLaporanToDetailLaporan(it.id)
                )
                viewModel.doneToDetail()
            }
        })

        viewModel.listLaporan.observe(viewLifecycleOwner, Observer {
            it?.let{
                if(it.isEmpty()){
                    binding.emptyState.visibility = View.VISIBLE
                }
                Log.d(DEFAULT_TAG,it.toString())
                (binding.rvLaporanListing.adapter as DaftarLaporanRVAdapter).submitList(it)
            }
        })
    }
}