package com.pedo.laporkan.ui.beranda

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedo.laporkan.R
import com.pedo.laporkan.databinding.FragmentBerandaBinding
import com.pedo.laporkan.ui.beranda.widget.LaporanTerkiniRVAdapter
import com.pedo.laporkan.ui.laporan.listing.DaftarLaporanRVAdapter

/**
 * A simple [Fragment] subclass.
 */
class BerandaFragment : Fragment() {
    private lateinit var binding: FragmentBerandaBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireNotNull(activity).application)
        ).get(BerandaViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showExitDialog(){
        val msg = SpannableStringBuilder()
            .append("Anda akan keluar dari aplikasi\n")
            .bold { append("LaporKan") }

        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Tindakan")
            .setMessage(msg)
            .setPositiveButton("Keluar") { _, _ ->
                findNavController().navigateUp()
            }
            .setNegativeButton("Batal"){ _,_ ->
                //do nothing
            }
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBerandaBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.latestLaporan.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.isNotEmpty()){
                    binding.imgEmptyTerkini.visibility = View.GONE

                    binding.rvLaporanTerkini.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                    val adapter = LaporanTerkiniRVAdapter(DaftarLaporanRVAdapter.OnClickListener { item ->
                        findNavController().navigate(
                            BerandaFragmentDirections.berandaToDetailLaporan(item.id)
                        )
                    })
                    adapter.submitList(it)
                    binding.rvLaporanTerkini.adapter = adapter
                }
            }
        })

        viewModel.toProfil.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        BerandaFragmentDirections.berandaToProfil()
                    )
                    viewModel.doneToProfil()
                }
            }
        })

        viewModel.berandaRole.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it == "Masyarakat"){
                    binding.imgMidLeftCard.setImageResource(R.drawable.ic_add)
                    binding.tvMidLeftCard.text = "Buat Laporan"
                }else {
                    binding.imgMidLeftCard.setImageResource(R.drawable.ic_insert_drive_file)
                    binding.tvMidLeftCard.text = "Buat Report"
                }
            }
        })

        viewModel.toBuatLaporan.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        BerandaFragmentDirections.berandaToBuatLaporan()
                    )
                    viewModel.doneNavigating()
                }
            }
        })

        viewModel.toListLaporan.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        BerandaFragmentDirections.berandaToDaftarLaporan()
                    )
                    viewModel.doneNavigating()
                }
            }
        })

        viewModel.toReport.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        BerandaFragmentDirections.berandaToDaftarReport()
                    )
                    viewModel.doneNavigating()
                }
            }
        })
    }
}
