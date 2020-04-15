package com.pedo.laporkan.ui.laporan.listing

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pedo.laporkan.R
import com.pedo.laporkan.databinding.FragmentDaftarLaporanBinding
import com.pedo.laporkan.utils.Constants
import com.pedo.laporkan.utils.Constants.SharedPrefKey.LOGGED_USER_ROLE

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
        binding.laporanTabLayout.setupWithViewPager(binding.laporanViewPager)

        return binding.root
    }

}
