package com.destack.hwmonitor.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONObject

class Computer(coreCounts: Int, val memoryTotal: Long, private val disks: List<StorageDisk>) {

    // CPU related
    val cpuCores: List<CPUCore> = List(coreCounts + 1) { CPUCore(it) }

    // Memory related variables
    private val _memoryAvailable = MutableLiveData(0L)

    // Storage related variables
    private val _storageDisks: MutableLiveData<List<StorageDisk>> = MutableLiveData(disks)

    /* Encapsulation */
    val memoryAvailable: LiveData<Long> = _memoryAvailable
    val storageDisks: LiveData<List<StorageDisk>> = _storageDisks

    fun update(json: JSONObject) {
        // CPU related data
        val cpuUsages = json.getJSONArray("cpu_usages")
        val cpuTemperatures = json.getJSONArray("cpu_temps")
        for (index in 0 until cpuUsages.length()) {
            val usage = cpuUsages.getDouble(index)
            val temperature =  cpuTemperatures.getDouble(index)
            cpuCores[index].update(usage.toInt(), temperature.toInt())
        }

        // Memory related data
        _memoryAvailable.postValue(json.getLong("memory_available"))

        // Storage related data
        val diskUsages = json.getJSONArray("drive_usages")
        for (index in 0 until diskUsages.length()) {
            val usage = diskUsages.getLong(index)
            disks[index].update(usage)
            _storageDisks.postValue(disks)
        }
    }

}