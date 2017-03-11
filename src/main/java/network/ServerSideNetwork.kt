package main.java.network

import java.net.ServerSocket
import java.net.Socket
import java.io.InputStreamReader
import java.io.BufferedReader
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

/**
 * Created by Mark Chimes on 2017/03/11.
 */

class ServerSideNetwork(portNumber : Int = 0, timeOut : Int = 0) {
    var server_port_number = portNumber ; private set
    var TIME_OUT = timeOut
    var isReadingMessages : Boolean = true
    val serverSocket: ServerSocket
    val messageQueue : BlockingQueue<String>

    init {
        serverSocket = ServerSocket(server_port_number)
        if (TIME_OUT > 0) serverSocket.soTimeout = TIME_OUT
        server_port_number = serverSocket.localPort
        messageQueue = LinkedBlockingQueue<String>()
    }

    fun closeServer() {
        serverSocket.close()
    }

    fun setupServerAndStartListening() {
        println("Server port test")
        try {
            ServerSideClientListenerThread(serverSocket, messageQueue).start()
            println("Socket port: " + server_port_number)
            var count = 0
            while (isReadingMessages && count < 6) {
                println("Trying to read messages")
                var message = messageQueue.take()
                println("MESSAGE RECIEVED!!!: " + message)
                count++
            }
        } finally {
            println("Shutting down server")
            closeServer()
        }
    }
}

class ServerSideClientListenerThread(serverSocket : ServerSocket, messageQueue : BlockingQueue<String>) : Thread() {
    val serverSocket = serverSocket
    var isListening : Boolean = true
    val messageQueue = messageQueue

    override fun run() {
        println("ServerSideClientListenerThread started")
        waitForClients()
    }

    fun waitForClients() {
        while (isListening) {
            var clientSocket = serverSocket.accept()
            println("We got one!")
            ServerSideNetworkThread(clientSocket, messageQueue).start()
        }
    }
}

class ServerSideNetworkThread(clientSocket : Socket, messageQueue : BlockingQueue<String>) : Thread() {
    val messageQueue = messageQueue
    val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    val socket = clientSocket
    var isAddingMessages = true
    val portNumber = socket.port

    override fun run() {
        println("ServerSideNetworkThread started with address: " +
                "${socket.localAddress} and port $portNumber")
        addMessagesToQueue()
    }

    fun addMessagesToQueue() {
        while (isAddingMessages) {
            println("Trying to read message on port: $portNumber" )
            var message : String? = reader.readLine()
            println("Got message on port: $portNumber reading $message" )
            if (message == null) {
                isAddingMessages = false
                socket.close()
            } else {
                messageQueue.put(message)
            }
        }
        println("Ending connection to socket: $socket")
    }
}

fun main(args : Array<String>) {
    val serverPort = ServerSideNetwork(1234, 0)
    serverPort.setupServerAndStartListening()
}