package com.pedo.laporkan.ui.laporan.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pedo.laporkan.databinding.FragmentDaftarLaporanBinding

/**
 * A simple [Fragment] subclass.
 */
class DaftarLaporanFragment : Fragment() {
    private lateinit var binding : FragmentDaftarLaporanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaftarLaporanBinding.inflate(inflater)

        val adapter = DaftarLaporanVPAdapter(childFragmentManager)

        binding.laporanViewPager.adapter = adapter
        binding.laporanTabLayout.setupWithViewPager(binding.laporanViewPager,true)

        return binding.root
    }

}
