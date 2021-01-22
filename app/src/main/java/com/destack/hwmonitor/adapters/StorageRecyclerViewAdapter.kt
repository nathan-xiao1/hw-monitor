package com.destack.hwmonitor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.destack.hwmonitor.data.StorageDisk
import com.destack.hwmonitor.databinding.ItemStorageBinding

class StorageRecyclerViewAdapter(private val owner: LifecycleOwner) :
    RecyclerView.Adapter<StorageRecyclerViewAdapter.ViewHolder>() {

    var dataset: List<StorageDisk> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflate = LayoutInflater.from(parent.context)
        val binding = ItemStorageBinding.inflate(layoutInflate, parent, false)
        binding.lifecycleOwner = owner
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount() = dataset.size

    // Custom ViewHolder class
    class ViewHolder(private val binding: ItemStorageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: StorageDisk) {
            binding.disk = model
        }
    }
}