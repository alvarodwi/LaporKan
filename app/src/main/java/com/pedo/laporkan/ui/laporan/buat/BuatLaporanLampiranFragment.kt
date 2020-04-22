package com.pedo.laporkan.ui.laporan.buat

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pedo.laporkan.databinding.FragmentBuatLaporanLampiranBinding
import com.pedo.laporkan.utils.Constants.IMAGE_TYPE
import com.pedo.laporkan.utils.Constants.REQUEST_CODE_CAMERA
import com.pedo.laporkan.utils.Constants.REQUEST_CODE_GALLERY

/**
 * A simple [Fragment] subclass.
 */
class BuatLaporanLampiranFragment : Fragment() {
    private lateinit var binding: FragmentBuatLaporanLampiranBinding
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
        binding = FragmentBuatLaporanLampiranBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)

        val arguments = BuatLaporanLampiranFragmentArgs.fromBundle(arguments!!)
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

        binding.btnRemoveImage.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Hilangkan Foto")
                .setMessage("Anda akan menghapus foto ini dari lampiran laporan Anda")
                .setPositiveButton("Ya") { _, _ ->
                    viewModel.removePhotoFromLaporan()
                }
                .setNegativeButton("Tidak"){ _,_ ->
                    //do nothing
                }
                .show()
        }

        viewModel.isPhotoAttached.observe(viewLifecycleOwner, Observer {
            if(it != null){
                binding.btnLanjutkan.text = "Lanjutkan"
                binding.btnRemoveImage.visibility = View.VISIBLE
                binding.previewLampiran.setImageBitmap(it)
            }else{
                binding.btnLanjutkan.text = "Lewati"
                binding.btnRemoveImage.visibility = View.GONE
                binding.previewLampiran.setImageBitmap(null)
            }
        })

        viewModel.photoAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    1 -> {
                        //start camera intent
                        startCameraIntent()
                    }
                    2 -> {
                        //start gallery intent
                        startGalleryIntent()
                    }
                    else -> {
                        //do nothing
                    }
                }
                viewModel.resetPhotoAction()
            }
        })

        viewModel.nextAction.observe(viewLifecycleOwner, Observer {
            it?.let{
                when(it){
                    2 -> {
                        findNavController().navigate(
                            BuatLaporanLampiranFragmentDirections.lampiranToTinjau(viewModel.getIncompleteLaporan())
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

    private fun startCameraIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{cameraIntent ->
            cameraIntent.resolveActivity(requireContext().packageManager)?.also {
                startActivityForResult(cameraIntent,REQUEST_CODE_CAMERA)
            }
        }
    }

    private fun startGalleryIntent(){
        Intent().apply {
            type = IMAGE_TYPE
            action = Intent.ACTION_GET_CONTENT
        }.also {galleryIntent ->
            galleryIntent.resolveActivity(requireContext().packageManager)?.also {
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != RESULT_OK){
            return
        }
        if(requestCode == REQUEST_CODE_CAMERA){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            viewModel.assignPhotoToLaporan(imageBitmap)
        }
        if(requestCode == REQUEST_CODE_GALLERY){
            data?.let {
                val imageBitmap = getImageBitmap(requireActivity().contentResolver,it.data!!)
                viewModel.assignPhotoToLaporan(imageBitmap)
            }
        }
    }

    private fun getImageBitmap(contentResolver: ContentResolver,path : Uri) : Bitmap{
        @Suppress("DEPRECATION") return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver,path))
        }else{
            MediaStore.Images.Media.getBitmap(contentResolver,path)
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
