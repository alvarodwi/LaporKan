package com.pedo.laporkan.ui.laporan.listing

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pedo.laporkan.ui.laporan.listing.list.LaporanYangDibuatFragment
import com.pedo.laporkan.ui.laporan.listing.list.LaporanYangDitanggapiFragment
import com.pedo.laporkan.ui.laporan.listing.list.SemuaLaporanFragment

class DaftarLaporanVPAdapter(private val userRole : String,fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = SemuaLaporanFragment()
            1 -> {
                when(userRole){
                    "Masyarakat" -> fragment = LaporanYangDibuatFragment()
                    else -> fragment = LaporanYangDitanggapiFragment()
                }
            }
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = 2
}