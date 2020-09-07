package com.destack.hwmonitor.network

import android.os.AsyncTask
import com.destack.hwmonitor.fragments.CPUViewModel
import okhttp3.OkHttpClient
import okhttp3.Request

class ServerRequestTask(vm: CPUViewModel) : AsyncTask<String, Void, String>() {

    private val client = OkHttpClient()
    private val viewModel: CPUViewModel = vm

    override fun doInBackground(vararg params: String?): String? {
        val request = Request.Builder()
            .url("http://192.168.1.106:8000")
            .build()
        val response = client.newCall(request).execute()
        return response.body()?.string()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        viewModel.updateResponse(result.toString())
    }
}

