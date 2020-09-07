package com.destack.hwmonitor.network

import android.os.AsyncTask
import android.util.Log
import com.destack.hwmonitor.fragments.CPUViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class ServerRequestTask(vm: CPUViewModel) : AsyncTask<String, Void, String>() {

    private var client = OkHttpClient.Builder()
        .connectTimeout(500, TimeUnit.MILLISECONDS)
        .readTimeout(500, TimeUnit.MILLISECONDS)
        .build()

    private val viewModel: CPUViewModel = vm

    override fun doInBackground(vararg params: String?): String? {
        val request = Request.Builder()
            .url("http://192.168.1.106:8000")
            .build()
        return try {
            val response = client.newCall(request).execute()
            response.body()?.string()
        } catch (e : SocketTimeoutException) {
            return null
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result == null) {
            Log.d("ServerRequestTask", "Request timed out")
        } else {
            viewModel.updateResponse(result.toString())
        }
    }
}

