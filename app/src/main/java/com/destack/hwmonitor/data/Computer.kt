package com.destack.hwmonitor.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONObject
import kotlin.math.max
import kotlin.math.min

class Computer(coreCounts: Int) {

    // CPU related
    val logicalProcessors: List<LogicalProcessor> = List(coreCounts) { LogicalProcessor(it) }
    private val _cpuUsagePackage = MutableLiveData(-1)
    private val _cpuUsagePackageMin = MutableLiveData(Integer.MAX_VALUE)
    private val _cpuUsagePackageMax = MutableLiveData(-1)

    // Memory related variables
    private val _memoryAvailable = MutableLiveData(0.0)

    // Storage related variables
    private val _storageDisks: MutableLiveData<List<List<String>>> = MutableLiveData(null)

    /* Encapsulation */
    val cpuUsagePackage: LiveData<Int> = _cpuUsagePackage
    val cpuUsagePackageMin: LiveData<Int> = _cpuUsagePackageMin
    val cpuUsagePackageMax: LiveData<Int> = _cpuUsagePackageMax
    val memoryAvailable: LiveData<Double> = _memoryAvailable
    val storageDisks: LiveData<List<List<String>>> = _storageDisks

    fun update(json: JSONObject) {
        // CPU related data
        val packageUsage = json.getInt("cpu_usage_package")
        _cpuUsagePackage.postValue(packageUsage)
        _cpuUsagePackageMin.postValue(min(_cpuUsagePackageMin.value!!, packageUsage))
        _cpuUsagePackageMax.postValue(max(_cpuUsagePackageMax.value!!, packageUsage))

        val logicalUsages = json.getJSONArray("cpu_usage_percpu")
        for (index in 0 until logicalUsages.length()) {
            val usage = logicalUsages.getDouble(index)
            logicalProcessors[index].update(usage.toInt(), 32)
        }

        // Memory related data
        _memoryAvailable.postValue(json.getDouble("memory_available"))

//        // Parse the disk JSON (array of array of string)
//        val disks = json.getJSONArray("disk_partitions")
//        val storageDisksList = ArrayList<ArrayList<String>>()
//        for (disk in 0 until disks.length()) {
//            val diskInfo = ArrayList<String>()
//            val diskInfoJSON = disks.getJSONArray(disk)
//            for (info in 0 until diskInfoJSON.length()) {
//                diskInfo.add(diskInfoJSON.getString(info))
//            }
//            storageDisksList.add(diskInfo)
//        }
//        _storageDisks.postValue(storageDisksList)
    }

}