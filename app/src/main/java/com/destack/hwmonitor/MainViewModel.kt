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
    private val _response = MutableLiveData(Pair(200, "No Response"))

    // CPU related variables
    private val _cpuUsagePackage = MutableLiveData(0)

    // Memory related variables
    private val _memoryAvailable = MutableLiveData(0.0)

    // Storage related variables
    private val _storage_disks: MutableLiveData<List<List<String>>> = MutableLiveData(null)

    /* Encapsulation */
    val response: LiveData<Pair<Int, String>> = _response
    val cpuUsagePackage: LiveData<Int> = _cpuUsagePackage
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
        _cpuUsagePackage.value = json.getInt("cpu_usage_package")
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
        Log.d("ADASDASDASDASDAS", _storage_disks.value.toString())
    }

}