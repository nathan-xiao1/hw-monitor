package com.destack.hwmonitor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONException
import org.json.JSONObject

class MainViewModel : ViewModel() {

    /* Private variables */
    // Response data
    private val _response = MutableLiveData("No response")

    // CPU related variables
    private val _cpu_usage_package = MutableLiveData(0)

    /* Encapsulation */
    val response: LiveData<String> = _response
    val cpu_usage_package: LiveData<Int> = _cpu_usage_package

    fun updateResponse(response: String) {
        _response.value = response
        try {
            val json = JSONObject(response)
            parseJSON(json)
        } catch (e : JSONException) {
            e.printStackTrace()
        }
    }

    private fun parseJSON(json: JSONObject) {
        _cpu_usage_package.value = json.getInt("cpu_usage_package")
    }

}