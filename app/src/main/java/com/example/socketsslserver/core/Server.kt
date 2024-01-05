package com.example.socketsslserver.core

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.certificates.saveToFile
import io.ktor.utils.io.core.use
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.Dispatchers
import java.io.File
import kotlin.math.ln


suspend fun startServer(ipAddress: String) {
    println("Server Start...")
    println("IpAddress : $ipAddress")

    val keyStoreFile = File("/Users/sukrit_love/AndroidStudioProjects/SocketSSLServer-Ktor/app/build/keystore.jks")
    println(keyStoreFile)
    try {
//        val keyStore =
//            buildKeyStore {
//                certificate("sampleAlias") {
//                    password = "foobar"
//                    domains = listOf("192.168.101.149", "192.168.101.101")
//                }
//            }
       // keyStore.saveToFile(keyStoreFile, "123456")
    } catch (e: Exception) {
        println("keyStore Error : $e")
    }



    val selectorManager = SelectorManager(Dispatchers.IO)
    val serverSocket = aSocket(selectorManager).tcp().bind(ipAddress, 4001)

    serverSocket.use {
        println("Server :: Listening at ${serverSocket.localAddress}")

        while (true) {
            val socket = serverSocket.accept()
            println("Status:: Accepted ${socket.remoteAddress}")

            val receiveChannel = socket.openReadChannel()
            val sendChannel = socket.openWriteChannel(autoFlush = true)
            try {

                val message: String? = receiveChannel.readUTF8Line()
                println(message)
                ClientMessage.messages = MessageData(
                    ClientMessage.messages.message + "${message ?: ""}\n"
                )
                sendChannel.writeStringUtf8("[Receive] your message is \"$message\"")
            } catch (e: Throwable) {
                println("Error : $e")
            } finally {
                socket.close()
            }
        }
    }
}

