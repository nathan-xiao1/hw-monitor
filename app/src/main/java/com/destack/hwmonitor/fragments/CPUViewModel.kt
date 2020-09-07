package com.destack.hwmonitor.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONException
import org.json.JSONObject

class CPUViewModel : ViewModel() {

    // CPU Usage variables
    private val _usage_package = MutableLiveData(0)
    private val _response = MutableLiveData("No response")

    val usage_package: LiveData<Int> = _usage_package
    val response: LiveData<String> = _response

    fun parseJSON(json : JSONObject) {
        _usage_package.value = json.getInt("cpu_usage_package")
    }

    fun updateResponse(response: String) {
        _response.value = response
        try {
            val json = JSONObject(response)
            parseJSON(json)
        } catch (e : JSONException) {
            e.printStackTrace()
        }
    }

}