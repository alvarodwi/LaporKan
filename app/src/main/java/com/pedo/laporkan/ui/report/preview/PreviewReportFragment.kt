package com.pedo.laporkan.ui.report.preview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.pedo.laporkan.R
import com.pedo.laporkan.databinding.FragmentPreviewReportBinding
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

        return binding.root
    }

}
