package com.destack.hwmonitor.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StorageDisk(
    val label: String,
    val fstype: String,
    val capacity: Long
) {

    companion object {
        const val THRESHOLD = 90
    }

    private val _usage = MutableLiveData(0)
    private val _used = MutableLiveData(0L)

    val usage: LiveData<Int> = _usage
    val used: LiveData<Long> = _used

    fun update(used: Long) {
        _used.postValue(used)
        _usage.postValue((used.toDouble() * 100 / capacity).toInt())
    }

}