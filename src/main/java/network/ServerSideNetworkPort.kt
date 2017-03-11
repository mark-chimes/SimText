package main.java.network

import java.net.ServerSocket
import java.net.Socket
import java.io.InputStreamReader
import java.io.BufferedReader



/**
 * Created by Mark Chimes on 2017/03/11.
 */

class ServerSideNetworkPort(portNumber : Int) {
    var server_port_number = portNumber ; private set

    lateinit var serverSocket: ServerSocket
    lateinit var clientSocket : Socket

    fun startServer() {
        serverSocket = ServerSocket(server_port_number)
        server_port_number = serverSocket.localPort
    }

    fun waitForClient() : BufferedReader {
        serverSocket.soTimeout = 20000
        clientSocket = serverSocket.accept()
        return BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    }

    fun closeServer() {
        serverSocket.close()
    }

    fun getMessage() {

    }

    fun setupServerAndStartListening() {
        println("Server port test")
        try {
            startServer()
            val inputReader = waitForClient()
            println("Socket port: " + server_port_number)
            Thread.sleep(1000)
            println("Line received: " + inputReader.readLine())
        } finally {
            println("Shutting down server")
            closeServer()
        }
    }
}

fun main(args : Array<String>) {
    val serverPort = ServerSideNetworkPort(1234)
    serverPort.setupServerAndStartListening()
}