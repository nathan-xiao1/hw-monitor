package com.destack.hwmonitor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.destack.hwmonitor.data.Computer
import com.destack.hwmonitor.network.ServerRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

const val RETRY_DELAY_MS: Long = 1000

class MainViewModel : ViewModel() {

    /* Setting Variables */
    var host: String = "10.0.2.2"
    var port: Int = 16779
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
        hostPC = Computer(json.getInt("cpu_count"))
        _ready.postValue(true)

        // Continuously request data from server
        while (true) {
            response = server.request()
            withContext(Dispatchers.Default) {
                updateResponse(response)
            }
            delay(RETRY_DELAY_MS)
        }
    }

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