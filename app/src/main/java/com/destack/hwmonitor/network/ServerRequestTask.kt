package com.destack.hwmonitor.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

val client = OkHttpClient.Builder()
    .connectTimeout(500, TimeUnit.MILLISECONDS)
    .readTimeout(500, TimeUnit.MILLISECONDS)
    .build()

/**
 * Suspendable function to request data from server
 * @param host The host address of the server
 * @return a JSON string of the response
 */
@Suppress("BlockingMethodInNonBlockingContext")
suspend fun serverRequest(host: String): String? {
    // Move the execution of the coroutine to the I/O dispatcher
    return withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(host)
                .build()
            Log.d("ServerRequestTask", "Requesting on $host")
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
}

