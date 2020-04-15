package com.pedo.laporkan.ui.laporan.listing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedo.laporkan.data.model.Laporan
import com.pedo.laporkan.data.model.relational.LaporanAndUser
import com.pedo.laporkan.databinding.ItemLaporanBinding

class DaftarLaporanRVAdapter(private val onClickListener: OnClickListener) : ListAdapter<LaporanAndUser,DaftarLaporanRVAdapter.LaporanVH>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarLaporanRVAdapter.LaporanVH {
        val binding = ItemLaporanBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LaporanVH(binding)
    }

    override fun onBindViewHolder(holder: DaftarLaporanRVAdapter.LaporanVH, position: Int) {
        holder.bind(getItem(position),onClickListener)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<LaporanAndUser>(){
        override fun areItemsTheSame(oldItem: LaporanAndUser, newItem: LaporanAndUser): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LaporanAndUser, newItem: LaporanAndUser): Boolean {
            return oldItem.laporan.id == newItem.laporan.id
        }
    }

    class LaporanVH(val binding : ItemLaporanBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : LaporanAndUser,itemClick : OnClickListener){
            binding.item = item
            binding.onClickListener = itemClick
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (item : Laporan) -> Unit){
        fun onClick(item: Laporan) = clickListener(item)
    }
}