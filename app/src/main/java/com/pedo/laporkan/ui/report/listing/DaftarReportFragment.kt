package com.pedo.laporkan.ui.report.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.pedo.laporkan.databinding.FragmentDaftarReportBinding
import com.pedo.laporkan.utils.Helpers.indonesianLocale
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

        viewModel.showMonthSelector.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    viewModel.resetAction()
                    createDatePickerDialog().let {monthpicker ->
                        monthpicker.show(childFragmentManager, null)
                        monthpicker.setOnDateSetListener { year, monthOfYear ->
                            val cal = Calendar.getInstance()
                            cal.set(year,monthOfYear,1)
                            navigateToPreviewReport(cal.time)
                            monthpicker.dismiss()
                        }
                    }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToPreviewReport(date : Date){
        findNavController().navigate(
            DaftarReportFragmentDirections.daftarReportToPreviewReport(date.time)
        )
    }

    private fun createDatePickerDialog(): MonthYearPickerDialogFragment {
        val calendar = Calendar.getInstance()
        return MonthYearPickerDialogFragment.getInstance(
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR),
            "Pilih bulan",
            indonesianLocale
        )
    }
}
