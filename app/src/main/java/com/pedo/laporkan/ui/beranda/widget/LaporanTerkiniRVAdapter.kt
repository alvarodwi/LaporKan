package com.pedo.laporkan.ui.beranda.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedo.laporkan.R
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.databinding.ItemLaporanTerkiniBinding
import com.pedo.laporkan.ui.laporan.listing.DaftarLaporanRVAdapter.OnClickListener
import com.pedo.laporkan.utils.Helpers

class LaporanTerkiniRVAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Laporan, LaporanTerkiniRVAdapter.LaporanTerkiniVH>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LaporanTerkiniVH {
        val binding =
            ItemLaporanTerkiniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaporanTerkiniVH(binding)
    }

    override fun onBindViewHolder(holder: LaporanTerkiniVH, position: Int) {
        holder.bind(getItem(position),onClickListener)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Laporan>() {
        override fun areItemsTheSame(oldItem: Laporan, newItem: Laporan): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Laporan, newItem: Laporan): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class LaporanTerkiniVH(val binding: ItemLaporanTerkiniBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Laporan,onClickListener: OnClickListener) {
            binding.tvJudulLaporanTerkini.text = item.judul
            binding.tvTanggalLaporanTerkini.text = item.tanggal.format(Helpers.laporKanDateFormat)
            item.foto.let { bm ->
                if (bm == null) {
                    binding.fotoLaporanTerkini.setImageDrawable(
                        binding.fotoLaporanTerkini.context.getDrawable(
                            R.drawable.ic_no_image
                        )
                    )
                    binding.fotoLaporanTerkini.scaleType = ImageView.ScaleType.CENTER_INSIDE
                } else {
                    binding.fotoLaporanTerkini.setImageBitmap(bm)
                }
            }

            binding.root.setOnClickListener {
                onClickListener.onClick(item)
            }
        }
    }
}