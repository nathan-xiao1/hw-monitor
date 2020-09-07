package com.destack.hwmonitor.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CPUViewModel : ViewModel() {

    // CPU Usage variables
    private val _usage_package = MutableLiveData(0)
    private val _response = MutableLiveData("No response")

    val usage_package: LiveData<Int> = _usage_package
    val response: LiveData<String> = _response

    fun updateUsage() {
        _usage_package.value = usage_package.value?.plus(1)
    }

    fun updateResponse(response: String) {
        _response.value = response
    }

}