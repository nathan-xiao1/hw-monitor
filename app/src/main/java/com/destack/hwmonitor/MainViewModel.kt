package com.destack.hwmonitor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.destack.hwmonitor.data.Computer
import com.destack.hwmonitor.data.StorageDisk
import com.destack.hwmonitor.network.ServerRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject

const val RETRY_DELAY_MS: Long = 1000

class MainViewModel : ViewModel() {

    /* Setting Variables */
    var host: String = "10.0.2.2"
    private var port: Int = 16779
    private val _ready = MutableLiveData(false)
    var ready: LiveData<Boolean> = _ready
    lateinit var hostPC: Computer
    private val server = ServerRequest(host, port)

    /* Data-Binding Variables */
    private val _response =
        MutableLiveData(Pair(200, "No Response")) // TODO: REMOVE, FOR DEBUG ONLY
    val response: LiveData<Pair<Int, String>> = _response // TODO: REMOVE, FOR DEBUG ONLY

    suspend fun startRequest() {
        // Request static info about host PC
        var response = server.request(init = true)
        while (response.first != 200) {
            response = server.request(init = true)
            delay(RETRY_DELAY_MS)
        }
        val json = JSONObject(response.second)
        initComputer(json)
        _ready.postValue(true)

        // Continuously request data from server
        while (true) {
            response = server.request()
            _response.postValue(response)
            if (response.first == 200) {
                withContext(Dispatchers.Default) {
                    hostPC.update(JSONObject(response.second))
                }
            }
            delay(RETRY_DELAY_MS)
        }
    }

    private fun initComputer(json: JSONObject) {
        // CPU data
        val cpuCount = json.getInt("cpu_count")
        // Memory data
        val memory = json.getLong("memory_total")
        // Storage disks data
        val disks = json.getJSONArray("disk_partitions")
        Log.d("MVM", "Disk Length: ${disks.length()}")
        val storageDisksList = ArrayList<StorageDisk>(disks.length())
        for (index in 0 until disks.length()) {
            val disk = disks.getJSONObject(index)
            val label = disk.getString("device")
            val fstype = disk.getString("fstype")
            val capacity = disk.getLong("capacity")
            storageDisksList.add(StorageDisk(label, fstype, capacity))
        }
        // Initialise Computer class
        this.hostPC = Computer(cpuCount, memory, storageDisksList)
    }

}