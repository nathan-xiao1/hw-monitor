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
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cpu, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        // Set up recycler view
        val customAdapter = CPURecyclerViewAdapter()
        binding.recyclerView.apply {
            adapter = customAdapter
            layoutManager = LinearLayoutManager(context)
        }

        customAdapter.dataset = listOf(MainViewModel.CPUCoreModel(0), MainViewModel.CPUCoreModel(1))

        return binding.root
    }

}