package com.destack.hwmonitor.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
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

val socket = DatagramSocket(16771)

suspend fun serverRequestUDP(host: String): Pair<Int, String> {
    var buffer = ByteArray(4096)

    // Set timeout for socket
    socket.soTimeout = 2000

    val m = "Hello!".toByteArray()
    val p = DatagramPacket(m, m.size, InetAddress.getByName("10.0.2.2"), 16779)
    socket.send(p)
    Log.d("Socket","Sent!")

    try {
        val packet = DatagramPacket(buffer, buffer.size)
        socket.receive(packet)
        // Update UI with the received data in main thread
        return Pair(200, String(packet.data))
    } catch (e: SocketTimeoutException) {
        Log.d("Socket","Socket timed out")
    }
    return Pair(400, "Timed out")
}




