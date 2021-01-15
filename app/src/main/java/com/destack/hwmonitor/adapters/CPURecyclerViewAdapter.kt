package com.destack.hwmonitor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.destack.hwmonitor.R

class CPURecyclerViewAdapter: RecyclerView.Adapter<CPURecyclerViewAdapter.ViewHolder>() {

    var dataset: List<Int>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cpu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset?.get(position) ?: 0, position)
    }

    override fun getItemCount() = dataset?.size ?: 0

    // Custom ViewHolder class
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cpuNumber = itemView.findViewById<TextView>(R.id.cpu_number_value)
        private val cpuUsage = itemView.findViewById<TextView>(R.id.cpu_usage_value)
        private val cpuTemperature = itemView.findViewById<TextView>(R.id.cpu_temp_current_value)

        fun bind(usage: Int, position: Int) {
            cpuNumber.text = itemView.context.getString(R.string.format_cpu_number, position)
            cpuUsage.text = itemView.context.getString(R.string.format_cpu_usage, usage)
            cpuTemperature.text = itemView.context.getString(R.string.format_cpu_temperature, 38)
        }
    }
}