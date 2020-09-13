package com.destack.hwmonitor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.destack.hwmonitor.R

class StorageRecyclerViewAdapter(): RecyclerView.Adapter<StorageRecyclerViewAdapter.ViewHolder>() {

    var dataset: List<List<String>>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // Create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.storage_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset?.get(position))
    }

    override fun getItemCount() = dataset?.size ?: 1

    // Custom ViewHolder class
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val diskLabel = itemView.findViewById<TextView>(R.id.disk_label)
        private val diskSublabel =itemView.findViewById<TextView>(R.id.disk_sublabel)

        fun bind(data: List<String>?) {
            if (data != null) {
                diskLabel.text = data[0]
                diskSublabel.text = "${data[2]} - ${data[3]}"
            }
        }

    }
}