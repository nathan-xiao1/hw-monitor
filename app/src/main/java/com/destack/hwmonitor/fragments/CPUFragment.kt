package com.destack.hwmonitor.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.destack.hwmonitor.R
import com.destack.hwmonitor.databinding.CpuFragmentBinding
import com.destack.hwmonitor.network.ServerRequestTask

class CPUFragment : Fragment() {

    companion object {
        fun newInstance() = CPUFragment()
    }

    private lateinit var mainHandler: Handler
    private lateinit var viewModel: CPUViewModel
    private lateinit var binding: CpuFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CPUViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.cpu_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTask)

        return binding.root
    }

    /**
     * Fetch data from server every 1 second in an AsyncTask
     */
    private val updateTask = object : Runnable {
        override fun run() {
            ServerRequestTask(viewModel).execute()
            mainHandler.postDelayed(this, 1000)
        }
    }

}