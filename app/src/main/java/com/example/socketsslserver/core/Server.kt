package com.example.socketsslserver.core

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.core.use
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO


suspend fun startServer(ipAddress: String) {
    println("Server Start...")
    println("IpAddress : $ipAddress")

    try {
        val selectorManager = SelectorManager(Dispatchers.IO)
        val serverSocket = aSocket(selectorManager).tcp().bind(ipAddress, 4001)

        serverSocket.use {
            println("Server :: Listening at ${serverSocket.localAddress}")

            while (true) {
                val socket = serverSocket.accept()
                println("Status:: Accepted $socket")

                val receiveChannel = socket.openReadChannel()
                val sendChannel = socket.openWriteChannel(autoFlush = true)
                try {

                    val message: String? = receiveChannel.readUTF8Line()
                    println(message)
                    ClientMessage.messages = MessageData(message?: "No message yet")
                    sendChannel.writeStringUtf8("[Receive] your message is \"$message\"")
                } catch (e: Throwable) {
                    println("Error : $e")
                } finally {
                    socket.close()
                }
            }
        }
    } catch (_e: Throwable) {
        println("Error $_e")
    }
}