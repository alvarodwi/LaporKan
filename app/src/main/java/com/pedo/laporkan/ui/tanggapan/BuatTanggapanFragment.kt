package com.pedo.laporkan.ui.tanggapan

import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.pedo.laporkan.databinding.FragmentBuatTanggapanBinding
/**
 * A simple [Fragment] subclass.
 */
class BuatTanggapanFragment : Fragment() {
    private lateinit var binding: FragmentBuatTanggapanBinding
    private lateinit var viewModel: BuatTanggapanViewModel
    private lateinit var bundle : BuatTanggapanFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuatTanggapanBinding.inflate(inflater)
        bundle =
            BuatTanggapanFragmentArgs.fromBundle(
                arguments!!
            )
        viewModel = ViewModelProvider(
            this,
            BuatTanggapanViewModel.Factory(
                bundle.idLaporan,
                requireActivity().application
            )
        ).get(BuatTanggapanViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                showCancelBuatTanggapanDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.appToolbar)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.let {
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
        }

        binding.btnContainer.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Tindakan")
                .setMessage("Anda akan mengirim tanggapan ini,\nsetelah dikirim, tanggapan\nAnda tidak bisa diubah")
                .setPositiveButton("Kirim") { _, _ ->
                    viewModel.onKirimTanggapanBtnClicked()
                    Toast.makeText(context,"Laporan terkirim!",Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("Batal"){ _,_ ->
                    //do nothing
                }
                .show()
        }

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    findNavController().navigate(
                        BuatTanggapanFragmentDirections.buatTanggapanToDetailLaporan(
                            bundle.idLaporan
                        )
                    )
                    viewModel.doneNextAction()
                }
            }
        })
    }

    private fun showCancelBuatTanggapanDialog(){
        val msg = SpannableStringBuilder()
            .append("Anda akan meninggalkan laman\n")
            .bold { append("Buat Tanggapan") }
            .append(". Tanggapan\nyang sedang Anda buat akan hilang")

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
