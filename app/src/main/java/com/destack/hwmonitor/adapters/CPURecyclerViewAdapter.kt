package com.destack.hwmonitor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.destack.hwmonitor.R
import com.destack.hwmonitor.data.CPUCore
import com.destack.hwmonitor.databinding.ItemCpuBinding

class CPURecyclerViewAdapter(private val owner: LifecycleOwner): RecyclerView.Adapter<CPURecyclerViewAdapter.ViewHolder>() {

    var dataset: List<CPUCore> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_cpu, parent, false)
        val layoutInflate = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemCpuBinding>(layoutInflate, R.layout.item_cpu, parent, false)
        binding.lifecycleOwner = owner
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset.get(position))
    }

    override fun getItemCount() = dataset.size

    // Custom ViewHolder class
    class ViewHolder(itemCpuBinding: ItemCpuBinding) : RecyclerView.ViewHolder(itemCpuBinding.root) {

        private var binding: ItemCpuBinding = itemCpuBinding

        fun bind(model: CPUCore) {
            binding.viewmodel = model
        }
    }

}