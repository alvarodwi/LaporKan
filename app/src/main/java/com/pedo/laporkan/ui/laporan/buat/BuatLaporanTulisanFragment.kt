package com.pedo.laporkan.ui.laporan.buat

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pedo.laporkan.databinding.FragmentBuatLaporanTulisanBinding

/**
 * A simple [Fragment] subclass.
 */
class BuatLaporanTulisanFragment : Fragment() {
    private lateinit var binding: FragmentBuatLaporanTulisanBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(BuatLaporanViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    showCancelBuatLaporanDialog()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuatLaporanTulisanBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.appToolbar)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.let{
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
        }

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    1 -> {
                        findNavController().navigate(
                            BuatLaporanTulisanFragmentDirections.buatLaporanToLampiran(viewModel.getIncompleteLaporan())
                        )
                        viewModel.doneNextAction()
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        })
    }

    private fun showCancelBuatLaporanDialog(){
        val msg = SpannableStringBuilder()
            .append("Anda akan meninggalkan laman\n")
            .bold { append("Buat Laporan") }
            .append(". Laporan yang\nsedang Anda buat akan hilang")

        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Tindakan")
            .setMessage(msg)
            .setPositiveButton("Ya") { _, _ ->
                findNavController().navigateUp()
            }
            .setNegativeButton("Tidak"){ _,_ ->
                //do nothing
            }
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
