package com.destack.hwmonitor.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.DatagramPacket
import java.net.DatagramSocket
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
suspend fun serverRequest(host: String): Pair<Int, String> {
    // Move the execution of the coroutine to the I/O dispatcher
    return withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(host)
                .build()
            Log.d("ServerRequestTask", "Requesting on $host")
            val response = client.newCall(request).execute()
            Pair(200, response.body?.string().toString())
        } catch (e: IllegalArgumentException) {
            Log.d("ServerRequestTask", "Host Not Found")
            Pair(404, "Host Not Found")
        } catch (e: SocketTimeoutException) {
            Log.d("ServerRequestTask", "Request Timeout")
            Pair(408, "Request Timeout")
        } catch (e: Throwable) {
            e.printStackTrace()
            Pair(0, "An error has occurred")
        }
    }
}

suspend fun serverRequestUDP(updateCallback: (String) -> Unit) {
    var buffer = ByteArray(4096)
    val socket = DatagramSocket(16779)
    var packet = DatagramPacket(buffer, buffer.size)

    // Set timeout for socket
    socket.soTimeout = 5000

    try {
        while (true) {
            socket.receive(packet)
            // Update UI with the received data in main thread
            withContext(Dispatchers.Main) {
                updateCallback(String(packet.data))
            }
        }
    } catch (e: SocketTimeoutException) {
        Log.d("Socket","Socket timed out")
    }
}




