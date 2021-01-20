package com.destack.hwmonitor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destack.hwmonitor.MainViewModel
import com.destack.hwmonitor.R
import com.destack.hwmonitor.adapters.StorageRecyclerViewAdapter

class StorageFragment : Fragment() {

    companion object {
        fun newInstance() = StorageFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_storage, container, false)

        // Set adapter for RecyclerView
        val customAdapter = StorageRecyclerViewAdapter()
        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = customAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.ready.observe(viewLifecycleOwner, { ready ->
            if (ready) {
                // Set observer for data change in disks
                viewModel.hostPC.storageDisks.observe(viewLifecycleOwner, {
                    it?.let {
                        customAdapter.dataset = it
                    }
                })
            }
        })

        return view
    }

}