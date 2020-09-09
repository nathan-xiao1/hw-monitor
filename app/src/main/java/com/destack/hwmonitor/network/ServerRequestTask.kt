package com.destack.hwmonitor.network

import android.os.AsyncTask
import android.util.Log
import com.destack.hwmonitor.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class ServerRequestTask(vm: MainViewModel) : AsyncTask<String, Void, String>() {

    private var client = OkHttpClient.Builder()
        .connectTimeout(500, TimeUnit.MILLISECONDS)
        .readTimeout(500, TimeUnit.MILLISECONDS)
        .build()

    private val viewModel: MainViewModel = vm

    override fun doInBackground(vararg params: String?): String? {
        return try {
            val request = Request.Builder()
            .url(viewModel.host)
            .build()
            Log.d("ServerRequestTask", "Requesting on ${viewModel.host}")
            val response = client.newCall(request).execute()
            response.body?.string()
        } catch (e: IllegalArgumentException) {
            Log.d("ServerRequestTask", "Invalid host")
            null
        } catch (e : SocketTimeoutException) {
            Log.d("ServerRequestTask", "Request timed out")
            null
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()) {
            viewModel.updateResponse(result.toString())
        }
    }
}

