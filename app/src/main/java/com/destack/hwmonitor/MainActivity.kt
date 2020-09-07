package com.destack.hwmonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.destack.hwmonitor.adapters.ComponentViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val adapter = ComponentViewPagerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set and initialise view pager adapter
        val viewPager = findViewById<ViewPager2>(R.id.component_viewpager)
        viewPager.adapter = adapter

        // Link view pager with tab layout
        val tabLayout = findViewById<TabLayout>(R.id.component_tab_layout)
        val components = resources.getStringArray(R.array.components)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = components[position]
        }.attach()
    }

}