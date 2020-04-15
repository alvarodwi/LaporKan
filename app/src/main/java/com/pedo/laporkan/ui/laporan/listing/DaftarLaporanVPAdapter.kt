package com.pedo.laporkan.ui.laporan.listing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pedo.laporkan.ui.laporan.listing.list.LaporanBaruFragment
import com.pedo.laporkan.ui.laporan.listing.list.LaporanProsesFragment
import com.pedo.laporkan.ui.laporan.listing.list.LaporanSelesaiFragment

class DaftarLaporanVPAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = LaporanBaruFragment()
            1 -> fragment = LaporanProsesFragment()
            2 -> fragment = LaporanSelesaiFragment()
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = 3
}