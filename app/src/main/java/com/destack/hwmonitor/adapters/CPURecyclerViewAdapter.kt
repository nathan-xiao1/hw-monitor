package com.destack.hwmonitor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.destack.hwmonitor.data.CPUCore
import com.destack.hwmonitor.databinding.ItemCpuBinding

class CPURecyclerViewAdapter(private val owner: LifecycleOwner) :
    RecyclerView.Adapter<CPURecyclerViewAdapter.ViewHolder>() {

    var dataset: List<CPUCore> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflate = LayoutInflater.from(parent.context)
        val binding = ItemCpuBinding.inflate(layoutInflate, parent, false)
        binding.lifecycleOwner = owner
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount() = dataset.size

    // Custom ViewHolder class
    class ViewHolder(private var binding: ItemCpuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CPUCore) {
            binding.viewmodel = model
        }
    }

}