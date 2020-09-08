package com.destack.hwmonitor.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.destack.hwmonitor.fragments.CPUFragment
import com.destack.hwmonitor.fragments.MemoryFragment

const val CPU_ITEM = 0
const val MEMORY_ITEM = 1
const val STORAGE_ITEM = 2

class ComponentViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            CPU_ITEM -> CPUFragment.newInstance()
            MEMORY_ITEM -> MemoryFragment.newInstance()
            else -> CPUFragment.newInstance()
        }
    }

}