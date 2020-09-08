package com.destack.hwmonitor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.destack.hwmonitor.MainViewModel
import com.destack.hwmonitor.R
import com.destack.hwmonitor.databinding.CpuFragmentBinding

class CPUFragment : Fragment() {

    companion object {
        fun newInstance() = CPUFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: CpuFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.cpu_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        return binding.root
    }

}