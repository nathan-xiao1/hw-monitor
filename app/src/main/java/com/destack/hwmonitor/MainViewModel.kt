package com.destack.hwmonitor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destack.hwmonitor.network.serverRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class MainViewModel : ViewModel() {

    // Start coroutine to request data from server continuously
    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val response = serverRequest(host)
                withContext(Dispatchers.Main) {
                    updateResponse(response)
                }
                delay(1000)
            }
        }
    }

    /* Private variables */
    // Settings
    var host: String = "http://192.168.1.106:8000"
        set(value) {
            field = "http://$value"
        }

    // Response data
    private val _error = MutableLiveData<Pair<Boolean, String?>>(Pair(false, null))
    private val _response = MutableLiveData(Pair(200, "No Response"))

    // CPU related variables
    private val _cpuUsagePackage = MutableLiveData(-1)
    private val _cpuUsagePackageMin = MutableLiveData(Integer.MAX_VALUE)
    private val _cpuUsagePackageMax = MutableLiveData(-1)

    // Memory related variables
    private val _memoryAvailable = MutableLiveData(0.0)

    // Storage related variables
    private val _storage_disks: MutableLiveData<List<List<String>>> = MutableLiveData(null)

    /* Encapsulation */
    val error: LiveData<Pair<Boolean, String?>> = _error
    val response: LiveData<Pair<Int, String>> = _response
    val cpuUsagePackage: LiveData<Int> = _cpuUsagePackage
    val cpuUsagePackageMin: LiveData<Int> = _cpuUsagePackageMin
    val cpuUsagePackageMax: LiveData<Int> = _cpuUsagePackageMax
    val memoryAvailable: LiveData<Double> = _memoryAvailable
    val storageDisks: LiveData<List<List<String>>> = _storage_disks

    private fun updateResponse(response: Pair<Int, String>) {
        _response.value = response
        if (response.first == 200) {
            try {
                val json = JSONObject(response.second)
                parseJSON(json)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun parseJSON(json: JSONObject) {

        // CPU related data
        _cpuUsagePackage.value = json.getInt("cpu_usage_package")
        _cpuUsagePackageMin.value = min(_cpuUsagePackageMin.value!!, _cpuUsagePackage.value!!)
        _cpuUsagePackageMax.value = max(_cpuUsagePackageMax.value!!, _cpuUsagePackage.value!!)

        // Memory related data
        _memoryAvailable.value = json.getDouble("memory_available")

        // Parse the disk JSON (array of array of string)
        val disks = json.getJSONArray("disk_partitions")
        val storageDisksList = ArrayList<ArrayList<String>>()
        for (disk in 0 until disks.length()) {
            val diskInfo = ArrayList<String>()
            val diskInfoJSON = disks.getJSONArray(disk)
            for (info in 0 until diskInfoJSON.length()) {
                diskInfo.add(diskInfoJSON.getString(info))
            }
            storageDisksList.add(diskInfo)
        }

        _storage_disks.value = storageDisksList
        Log.d("STORAGE_DEBUG", _storage_disks.value.toString())
    }

    // Model for individual core item on UI
    class CPUCoreModel(coreNumber: Int) {
        val coreNumber = coreNumber

        private val _usage = MutableLiveData<Int>(-1)
        private val _usageMin = MutableLiveData<Int>(Integer.MAX_VALUE)
        private val _usageMax = MutableLiveData<Int>(-1)

        private val _temperature = MutableLiveData<Int>(Random.nextInt(30, 70)) // TODO Replace
        private val _temperatureMin = MutableLiveData<Int>(Integer.MAX_VALUE)
        private val _temperatureMax = MutableLiveData<Int>(0)

        val usage: LiveData<Int> = _usage
        val usageMin: LiveData<Int> = _usageMin
        val usageMax: LiveData<Int> = _usageMax
        val temperature: LiveData<Int> = _temperature
        val temperatureMin: LiveData<Int> = _temperatureMin
        val temperatureMax: LiveData<Int> = _temperatureMax

        fun update(usage: Int, temperature: Int) {
            _usage.value = usage
            _usageMin.value = min(_usageMin.value!!, usage)
            _usageMax.value = max(_usageMax.value!!, usage)
            _temperature.value = temperature
            _temperatureMin.value = min(_temperatureMin.value!!, temperature)
            _temperatureMax.value = max(_temperatureMax.value!!, temperature)
        }

    }

}