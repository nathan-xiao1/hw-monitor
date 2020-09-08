package com.destack.hwmonitor

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.destack.hwmonitor.adapters.ComponentViewPagerAdapter
import com.destack.hwmonitor.network.ServerRequestTask
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    // Constants
    private val CPU_ITEM = 0
    private val MEMORY_ITEM = 1
    private val STORAGE_ITEM = 2

    // ViewModel
    private lateinit var viewModel: MainViewModel

    // Handler
    private lateinit var mainHandler: Handler

    // Navigation related variables
    private val adapter = ComponentViewPagerAdapter(this)
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create or get existing viewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Set and initialise view pager adapter
        viewPager = findViewById(R.id.component_viewpager)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(pageChangeCallback)

        // Set listener for bottom navigation bar
        bottomNavBar = findViewById(R.id.component_bottom_nav)
        bottomNavBar.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        // Add the updateTask to fetch data from server continuously
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTask)
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

    /**
     * OnNavigationItemSelectedListener for BottomNavigationView
     * Set the corresponding fragment view on ViewPager to be active
     */
    private val navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_item_cpu -> {
                    viewPager.currentItem = CPU_ITEM
                }
                R.id.bottom_nav_item_memory -> {
                    viewPager.currentItem = MEMORY_ITEM
                }
                R.id.bottom_nav_item_storage -> {
                    viewPager.currentItem = STORAGE_ITEM
                }
            }
            true
        }

    /**
     * OnPageChangeCallback for ViewPager
     * Set the corresponding menu item on BottomNavigationView to be active
     */
    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            when (position) {
                CPU_ITEM -> {
                    bottomNavBar.menu.findItem(R.id.bottom_nav_item_cpu).isChecked = true
                }
                MEMORY_ITEM -> {
                    bottomNavBar.menu.findItem(R.id.bottom_nav_item_memory).isChecked = true
                }
                STORAGE_ITEM -> {
                    bottomNavBar.menu.findItem(R.id.bottom_nav_item_storage).isChecked = true
                }
            }
        }
    }


}