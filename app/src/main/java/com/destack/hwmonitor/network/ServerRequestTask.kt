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
suspend fun serverRequestHTTP(host: String): Pair<Int, String> {
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

class ServerRequest(hostname: String, port: Int, bufSize: Int = 4096) {

    private val socket = DatagramSocket()
    private val buffer = ByteArray(bufSize)
    private var address = InetAddress.getByName(hostname)
    var port = port

    init {
        // Set timeout for socket
        socket.soTimeout = 2000
    }

    fun request(init: Boolean = false): Pair<Int, String> {
        // Send request to server
        val msg = (if (init) "S" else "D").toByteArray()
        socket.send(DatagramPacket(msg, msg.size, address, port))
        Log.d("SRT", "Request sent to ${address.hostName}:${port}")
        return try {
            val packet = DatagramPacket(buffer, buffer.size)
            socket.receive(packet)
            Pair(200, String(packet.data))
        } catch (e: SocketTimeoutException) {
            Pair(400, "Request Timed Out")
        }
    }

    fun setHost(hostname: String) {
        this.address = InetAddress.getByName(hostname)
    }

}




