package com.pedo.laporkan.ui.report.preview

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pedo.laporkan.data.model.StatusLaporan
import com.pedo.laporkan.databinding.FragmentPreviewReportBinding
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class PreviewReportFragment : Fragment() {
    private lateinit var binding: FragmentPreviewReportBinding
    private lateinit var viewModel: PreviewReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreviewReportBinding.inflate(inflater)
        viewModel = ViewModelProvider(
            this,
            PreviewReportViewModel.Factory(
                Date(PreviewReportFragmentArgs.fromBundle(arguments!!).dateLong)
                , requireActivity().application
            )
        ).get(PreviewReportViewModel::class.java)
        binding.viewModel = viewModel
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

        binding.btnContainer.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Tindakan")
                .setMessage("Report ini akan dicetak dan disimpan di penyimpanan perangkat Anda")
                .setPositiveButton("Oke") { _, _ ->
                    viewModel.onCetakPdfClicked()
                    Toast.makeText(context,"Report dicetak!",Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("Batal"){ _,_ ->
                    //do nothing
                }
                .show()
        }

        viewModel.laporanReportData.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d(DEFAULT_TAG,it.toString())
                var totalLaporanCount = 0
                var totalLaporanSelesai = 0
                var totalLaporanProses = 0
                var totalLaporanBaru = 0
                var totalLaporanGagal = 0

                for(laporanResponse in it){
                    totalLaporanCount += laporanResponse.laporanCount
                    when(laporanResponse.laporanStatus){
                        StatusLaporan.SELESAI -> {
                            totalLaporanSelesai = laporanResponse.laporanCount
                        }
                        StatusLaporan.PROSES -> {
                            totalLaporanProses = laporanResponse.laporanCount
                        }
                        StatusLaporan.BARU -> {
                            totalLaporanBaru = laporanResponse.laporanCount
                        }
                        StatusLaporan.GAGAL -> {
                            totalLaporanGagal = laporanResponse.laporanCount
                        }
                    }
                }

                binding.tvLaporanSemua.text = SpannableStringBuilder()
                    .append("Dari ")
                    .bold { append("$totalLaporanCount") }
                    .append(" laporan yang dibuat bulan ini")

                binding.tvLaporanSelesai.text = SpannableStringBuilder()
                    .bold { append("$totalLaporanSelesai") }
                    .append(" laporan selesai")

                binding.tvLaporanProses.text = SpannableStringBuilder()
                    .bold { append("$totalLaporanProses") }
                    .append(" laporan masih dalam proses")

                binding.tvLaporanBaru.text = SpannableStringBuilder()
                    .bold { append("$totalLaporanBaru") }
                    .append(" laporan belum divalidasi")

                binding.tvLaporanGagal.text = SpannableStringBuilder()
                    .bold { append("$totalLaporanGagal") }
                    .append(" laporan gagal validasi")
            }
        })

        viewModel.userReportData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.isEmpty()){
                    binding.btnContainer.visibility = View.GONE
                    binding.errorReport.visibility = View.VISIBLE
                    binding.cardReport.visibility = View.GONE
                }else{
                    binding.tvKontributor.text = SpannableStringBuilder()
                        .bold { append("${it.size}") }
                        .append(" petugas membuat tanggapan")
                }
            }
        })

        viewModel.petugasReportData.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvRespons.text = SpannableStringBuilder()
                    .bold { append("${it.size}") }
                    .append(" masyarakat berkontribusi")
            }
        })

        viewModel.openPdf.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    this.previewPdf()
                    viewModel.resetOpenPdf()
                }
            }
        })
    }

    private fun previewPdf(){
        val packageManager: PackageManager = requireActivity().packageManager
        val testIntent = Intent(Intent.ACTION_VIEW)
        testIntent.type = "application/pdf"
        val list: List<*> =
            packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list.isNotEmpty()) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            //            Uri uri = Uri.fromFile(pdfFile);
            val uri: Uri =
                FileProvider.getUriForFile(requireContext(), "com.pedo.laporkan.provider", viewModel.pdfFile)
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            intent.resolveActivity(packageManager)?.also {
                startActivity(intent)
            }
        } else {
            Toast.makeText(
                context,
                "Download Aplikasi Pdf Viewer Untuk Melihat Hasil Generate!",
                Toast.LENGTH_SHORT
            ).show()
        }
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
