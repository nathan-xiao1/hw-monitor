package com.destack.hwmonitor.adapters

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.destack.hwmonitor.fragments.CPUFragment

class ComponentViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return CPUFragment.newInstance()
    }

}