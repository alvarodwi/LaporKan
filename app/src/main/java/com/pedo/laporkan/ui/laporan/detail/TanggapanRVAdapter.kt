package com.pedo.laporkan.ui.laporan.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedo.laporkan.data.model.relational.TanggapanAndUser
import com.pedo.laporkan.databinding.ItemTanggapanBinding

class TanggapanRVAdapter : ListAdapter<TanggapanAndUser, TanggapanRVAdapter.TanggapanVH>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TanggapanVH {
        val binding = ItemTanggapanBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TanggapanVH(binding)
    }

    override fun onBindViewHolder(holder: TanggapanVH, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TanggapanAndUser>(){
        override fun areItemsTheSame(oldItem: TanggapanAndUser, newItem: TanggapanAndUser): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TanggapanAndUser, newItem: TanggapanAndUser): Boolean {
            return oldItem.tanggapan.id == newItem.tanggapan.id
        }
    }

    class TanggapanVH(val binding : ItemTanggapanBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : TanggapanAndUser){
            binding.item = item
            binding.executePendingBindings()
        }
    }
}