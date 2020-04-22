package com.pedo.laporkan.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pedo.laporkan.R
import com.pedo.laporkan.ui.intro.LaporKanIntro

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {
    private val mWaitHandler = Handler()
    private val viewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(requireActivity().application))
            .get(SplashViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let{
                when(it){
                    1 -> {
                        //arahkan ke intro
                        loadIntro()
                    }
                    2 -> {
                        //arahkan ke login
                        mWaitHandler.postDelayed({
                            try{
                                findNavController().navigate(
                                    SplashFragmentDirections.splashToLogin()
                                )
                            }catch(ignored : Exception){
                                ignored.printStackTrace()
                            }
                        },3000) // 1.5 seconds
                    }
                    3 -> {
                        //arahkan ke beranda
                        mWaitHandler.postDelayed({
                            try{
                                findNavController().navigate(
                                    SplashFragmentDirections.splashToBeranda()
                                )
                            }catch(ignored : Exception){
                                ignored.printStackTrace()
                            }
                        },3000) // 1.5 seconds
                    }
                    else -> {
                        //do nothing
                    }
                }
                viewModel.resetNextAction()
            }
        })
    }

    private fun loadIntro(){
        Intent(requireActivity(),LaporKanIntro::class.java).also { intentIntro ->
            intentIntro.resolveActivity(requireContext().packageManager)?.also {
                startActivity(intentIntro)
            }
        }
    }
}
