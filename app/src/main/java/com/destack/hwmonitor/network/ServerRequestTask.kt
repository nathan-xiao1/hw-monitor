package com.destack.hwmonitor.network

import android.util.Log
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

const val TIMEOUT_MS = 2000

class ServerRequest(hostname: String, private var port: Int, bufSize: Int = 4096) {

    private val socket = DatagramSocket()
    private val buffer = ByteArray(bufSize)
    private var address = InetAddress.getByName(hostname)

    init {
        // Set timeout for socket
        socket.soTimeout = TIMEOUT_MS
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

}




