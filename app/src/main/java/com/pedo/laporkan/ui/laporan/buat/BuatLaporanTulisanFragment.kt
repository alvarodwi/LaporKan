package com.pedo.laporkan.ui.laporan.buat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pedo.laporkan.R

/**
 * A simple [Fragment] subclass.
 */
class BuatLaporanTulisanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buat_laporan_tulisan, container, false)
    }

}
