package com.pedo.laporkan.ui.register

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pedo.laporkan.databinding.FragmentRegisterDataBinding
import com.pedo.laporkan.utils.Constants.DEFAULT_TAG

/**
 * A simple [Fragment] subclass.
 */
class RegisterDataFragment : Fragment() {
    private lateinit var binding: FragmentRegisterDataBinding
    private lateinit var viewModel : RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                showCancelRegistrationDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showCancelRegistrationDialog(){
        val msg = SpannableStringBuilder()
            .append("Anda akan meninggalkan laman\n")
            .bold { append("Registrasi") }
            .append(". Data user\nyang sedang Anda buat akan hilang")

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val arguments = RegisterDataFragmentArgs.fromBundle(arguments!!)
        Log.d(DEFAULT_TAG,arguments.userRole)
        binding = FragmentRegisterDataBinding.inflate(inflater)
        viewModel =
            ViewModelProvider(
                this,
                RegisterViewModel.Factory(
                    requireActivity().application,
                    arguments.userRole
                )
            ).get(RegisterViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context,it,Toast.LENGTH_LONG).show()
        })

        viewModel.toKodeAdminAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    findNavController().navigate(
                        RegisterDataFragmentDirections.registerToKodeAdmin()
                    )
                    viewModel.doneKodeAdminClicked()
                }
            }
        })

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    1 -> {
                        findNavController().navigate(
                            RegisterDataFragmentDirections.registerToRegister2(viewModel.getIncompleteUser())
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

}
