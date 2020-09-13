package com.destack.hwmonitor

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.destack.hwmonitor.adapters.CPU_ITEM
import com.destack.hwmonitor.adapters.ComponentViewPagerAdapter
import com.destack.hwmonitor.adapters.MEMORY_ITEM
import com.destack.hwmonitor.adapters.STORAGE_ITEM
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    // ViewModel
    private lateinit var viewModel: MainViewModel

    // Navigation related variables
    private val adapter = ComponentViewPagerAdapter(this)
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var viewPager: ViewPager2

    // Snackbar
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create SnackBar
        snackbar = Snackbar.make(
            findViewById(R.id.root_view),
            "An error has occurred",
            Snackbar.LENGTH_INDEFINITE
        )

        // Save the default values in shared preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        // Create or get existing viewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.host = sharedPref.getString("server_ip", "192.168.1.100") +
                ":" + sharedPref.getString("server_host", "8000")

//        // Set observer for response in ViewModel
//        viewModel.response.observe(this, Observer { response ->
//            if (response.first != 200 && !snackbar.isShown) {
//                snackbar.setText("Error code ${response.first}: ${response.second}")
//                snackbar.show()
//            }
//        })

        // Set toolbar as action bar
        setSupportActionBar(findViewById(R.id.toolbar))

        // Set and initialise view pager adapter
        viewPager = findViewById(R.id.component_viewpager)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(pageChangeCallback)

        // Set listener for bottom navigation bar
        bottomNavBar = findViewById(R.id.component_bottom_nav)
        bottomNavBar.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        // Set preference change listener
        sharedPref.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

    /**
     * OnSharedPreferenceChangeListener for SharedPreference
     * Update the IP/Port for network requests
     */
    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { p0, p1 ->
            when (p1) {
                "server_ip", "server_port" -> viewModel.host =
                    p0?.getString("server_ip", "192.168.1.100") + ":" + p0?.getString(
                        "server_host",
                        "8000"
                    )
            }
        }

}