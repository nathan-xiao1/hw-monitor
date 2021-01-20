package com.destack.hwmonitor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.destack.hwmonitor.MainViewModel
import com.destack.hwmonitor.R
import com.destack.hwmonitor.adapters.CPURecyclerViewAdapter
import com.destack.hwmonitor.databinding.FragmentCpuBinding

class CPUFragment : Fragment() {

    companion object {
        fun newInstance() = CPUFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentCpuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cpu, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // Set up recycler view
        val customAdapter = CPURecyclerViewAdapter(viewLifecycleOwner)
        binding.recyclerView.apply {
            adapter = customAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // Bind viewModel to UI when viewModel is ready
        viewModel.ready.observe(viewLifecycleOwner, { ready ->
            if (ready) {
                binding.layoutError.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.viewmodel = viewModel
                customAdapter.dataset = viewModel.hostPC.logicalProcessors
            }
        })

        return binding.root
    }

}