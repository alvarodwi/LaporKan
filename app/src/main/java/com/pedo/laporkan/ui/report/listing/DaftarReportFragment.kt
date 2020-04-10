package com.pedo.laporkan.ui.report.listing

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.pedo.laporkan.databinding.FragmentDaftarReportBinding
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG
import com.pedo.laporkan.utils.Helpers.indonesianLocale
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class DaftarReportFragment : Fragment() {
    private lateinit var binding: FragmentDaftarReportBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(DaftarReportViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaftarReportBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showMonthSelector.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    createDatePickerDialog()?.let {
                        it.show(childFragmentManager, null)
                        it.setOnDateSetListener(MonthYearPickerDialog.OnDateSetListener { year, monthOfYear ->
                            val cal = Calendar.getInstance()
                            cal.set(year,monthOfYear,1)
                            navigateToPreviewReport(cal.time)
                        })
                    }
                    viewModel.resetAction()
                }
            }
        })

        viewModel.selectedMonth.observe(viewLifecycleOwner, Observer {
            it?.let {
                //navigate to preview report
                navigateToPreviewReport(it)
                viewModel.resetAction()
            }
        })
    }

    private fun navigateToPreviewReport(date : Date){
        findNavController().navigate(
            DaftarReportFragmentDirections.daftarReportToPreviewReport(date.time)
        )
    }

    private fun createDatePickerDialog(): MonthYearPickerDialogFragment {
        val calendar = Calendar.getInstance()
        val monthYearPicker = MonthYearPickerDialogFragment.getInstance(
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR),
            "Pilih bulan",
            indonesianLocale
        )
        return monthYearPicker
    }
}
