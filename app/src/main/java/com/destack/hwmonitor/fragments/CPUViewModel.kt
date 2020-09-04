package com.destack.hwmonitor.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CPUViewModel : ViewModel() {

    // CPU Usage variables
    val _usage_package = MutableLiveData(0)

    val usage_package: LiveData<Int> = _usage_package

    fun updateUsage() {
        _usage_package.value = usage_package.value?.plus(1)
    }

}