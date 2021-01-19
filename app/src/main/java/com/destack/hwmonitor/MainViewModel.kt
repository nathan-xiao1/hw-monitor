package com.destack.hwmonitor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.destack.hwmonitor.data.Computer
import com.destack.hwmonitor.network.serverRequestUDP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

class MainViewModel : ViewModel() {

    //
    val hostPC = Computer(12)

    // Start coroutine to request data from server continuously
    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val response = serverRequestUDP(host)
                withContext(Dispatchers.Default) {
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

    private val _response = MutableLiveData(Pair(200, "No Response")) // TODO: REMOVE, FOR DEBUG ONLY

    /* Data binding variables */
    val response: LiveData<Pair<Int, String>> = _response // TODO: REMOVE, FOR DEBUG ONLY
    val cpuUsagePackage: LiveData<Int> = hostPC.cpuUsagePackage
    val cpuUsagePackageMin: LiveData<Int> = hostPC.cpuUsagePackageMin
    val cpuUsagePackageMax: LiveData<Int> = hostPC.cpuUsagePackageMax
    val memoryAvailable: LiveData<Double> = hostPC.memoryAvailable
    val storageDisks: LiveData<List<List<String>>> = hostPC.storageDisks

    private fun updateResponse(response: Pair<Int, String>) {
        _response.postValue(response)
        if (response.first == 200) {
            try {
                val json = JSONObject(response.second)
                hostPC.update(json)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

}