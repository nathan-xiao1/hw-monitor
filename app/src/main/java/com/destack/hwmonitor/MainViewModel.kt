package com.destack.hwmonitor

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
                if (!response.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        updateResponse(response)
                    }
                }
                delay(1000)
            }
        }
    }

    /* Private variables */
    // Settings
    var host: String = "http://192.168.1.100:8000"
        set(value) {
            field = "http://$value"
        }

    // Response data
    private val _response = MutableLiveData("No response")

    // CPU related variables
    private val _cpuUsagePackage = MutableLiveData(0)

    // Memory related variables
    private val _memoryAvailable = MutableLiveData(0.0)

    /* Encapsulation */
    val response: LiveData<String> = _response
    val cpuUsagePackage: LiveData<Int> = _cpuUsagePackage
    val memoryAvailable: LiveData<Double> = _memoryAvailable

    private fun updateResponse(response: String) {
        _response.value = response
        try {
            val json = JSONObject(response)
            parseJSON(json)
        } catch (e : JSONException) {
            e.printStackTrace()
        }
    }

    private fun parseJSON(json: JSONObject) {
        _cpuUsagePackage.value = json.getInt("cpu_usage_package")
        _memoryAvailable.value = json.getDouble("memory_available")
    }

}