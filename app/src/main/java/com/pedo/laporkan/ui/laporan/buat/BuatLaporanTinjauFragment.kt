package com.pedo.laporkan.ui.laporan.buat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pedo.laporkan.databinding.FragmentBuatLaporanTinjauBinding

/**
 * A simple [Fragment] subclass.
 */
class BuatLaporanTinjauFragment : Fragment() {
    private lateinit var binding: FragmentBuatLaporanTinjauBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(BuatLaporanViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuatLaporanTinjauBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)

        val arguments = BuatLaporanTinjauFragmentArgs.fromBundle(arguments!!)
        viewModel.setIncompleteLaporan(arguments.itemLaporan)

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

        binding.btnContainer.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Tindakan")
                .setMessage("Anda akan mengirim laporan ini,\nsetelah dikirim, laporan Anda\ntidak bisa diubah")
                .setPositiveButton("Kirim") { _, _ ->
                    viewModel.onKirimLaporanClicked()
                    Toast.makeText(context,"Laporan terkirim!",Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("Batal"){ _,_ ->
                    //do nothing
                }
                .show()
        }

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let{
                when(it){
                    3 -> {
                        findNavController().navigate(
                            BuatLaporanTinjauFragmentDirections.buatLaporanTinjauToBeranda()
                        )
                        viewModel.doneNextAction()
                    }
                    else -> {
                        //do nothing
                    }
                }
            }
        })
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
