package main.java.network

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import java.io.PrintWriter




/**
 * Created by Mark Chimes on 2017/03/11.
 */

class ClientSideNetwork {
    fun connectToHost(hostAddress: String, port : Int) {
        var socket : Socket? = null
        try {
            val socket = Socket(hostAddress, port)
            println("Connected: " + socket.isConnected + " to address ${socket.remoteSocketAddress}")
            val writer = PrintWriter(socket.getOutputStream(), true)
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

            for (i in 1..6) {
                println("Writing: Potato: " + socket.localPort + " " + i)
                writer.write("Potato: " + socket.localPort + " " + i + "\n")
                writer.flush()
                println("Reading incoming: " + socket.localPort + " " + i)
                val inMessage = reader.readLine()
                println("read message: " + inMessage)
                Thread.sleep(1000)
            }
        } finally {
            socket?.close()
        }
    }


}

fun main(args : Array<String>) {
    val network  = ClientSideNetwork()
    network.connectToHost("10.0.0.174", 1234)


}