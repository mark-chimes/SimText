package main.java.network

import main.java.client.base.CommandConsumer
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.PrintWriter
import java.net.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

/**
 * Created by Mark Chimes on 2017/03/11.
 */

class ServerSideNetwork(portNumber : Int = 0, timeOut : Int = 0) : CommandConsumer {
    val initial_port_number = portNumber
    var TIME_OUT = timeOut
    var isReadingMessages : Boolean = true
    var toCloseServerSocket : ServerSocket? = null
    val incomingMessageQueue: BlockingQueue<String>
    val outgoingMessageQueue : BlockingQueue<String>
    val POISON_STRING = "POISON_STRING" // TODO
    lateinit var listener : ServerSideClientListenerThread
    init {
        incomingMessageQueue = LinkedBlockingQueue<String>()
        outgoingMessageQueue = LinkedBlockingQueue<String>()
    }

    fun getLocalIpAddress() : String {
        try {
            return InetAddress.getLocalHost().toString()
        } catch (ex : UnknownHostException) {
            ex.printStackTrace()
            return "UNKNOWN LOCAL IP"
        }
    }

    fun closeServer() {
        toCloseServerSocket?.close()
        listener.stopThreadExternally()
    }

    fun setupServerAndStartListening() {
        try {
            val serverSocket = ServerSocket(initial_port_number)
            toCloseServerSocket = serverSocket
            if (TIME_OUT > 0) serverSocket.soTimeout = TIME_OUT
            val ipAddress = getLocalIpAddress()
            println("Starting server on $ipAddress/${serverSocket.localPort}.")
            listener = ServerSideClientListenerThread(serverSocket, incomingMessageQueue, outgoingMessageQueue)
            listener.start()
            while (isReadingMessages) {
                println("Looking for next incoming message")
                if (incomingMessageQueue.isNotEmpty()) {
                    println("Found a message!")
                    var message = incomingMessageQueue.take()
                    if (message == POISON_STRING) {
                        println("Received poison string")
                        isReadingMessages = false
                    } else {
                        println("Received message: " + message)
                    }
                    println("took incoming message")
                } else {
                    Thread.sleep(5000)
                }
                outgoingMessageQueue.put("Hi, Bobby!\n")
            }
        } catch (e : BindException) {
            println("Could not start server because host already bound to port $initial_port_number.\n" +
                    "Specify a different port number and try again. Use 0 to bind a free port automatically.")
        } catch (e : Exception) {
            println("Error in server start. Server startup cancelled")
            e.printStackTrace()
        } finally {
            println("Closing server")
            outgoingMessageQueue.put(POISON_STRING)
            closeServer()
        }
    }

    override fun sendMessage(message: String) {
        outgoingMessageQueue.put(message)
    }
}

class ServerSideClientListenerThread(serverSocket : ServerSocket, incomingMessageQueue: BlockingQueue<String>,
                                     outgoingMessageQueue: BlockingQueue<String>) : Thread() {
    val serverSocket = serverSocket
    var isListening : Boolean = true
    val incomingMessageQueue = incomingMessageQueue
    val outgoingMessageQueue = outgoingMessageQueue
    lateinit var incomingHandler : ServerSideNetworkIncomingMessageHandler
    lateinit var outgoingHandler : ServerSideNetworkOutgoingMessageHandler

    override fun run() {
        waitForClients()
    }

    fun stopThreadExternally() {
        isListening = false
        interrupt()
    }

    fun closeDownThread() {
        isListening = false
        incomingHandler.stopThreadExternally()
        outgoingHandler.stopThreadExternally()
    }

    fun waitForClients() {
        while (isListening) {
            try {
                val clientSocket = serverSocket.accept()
                println("My address is " + clientSocket.localAddress)
                println("Connected to client at address: ${clientSocket.remoteSocketAddress}")
                incomingHandler = ServerSideNetworkIncomingMessageHandler(clientSocket, incomingMessageQueue)
                outgoingHandler = ServerSideNetworkOutgoingMessageHandler(clientSocket, outgoingMessageQueue)
                incomingHandler.start()
                outgoingHandler.start()
            } catch (e : InterruptedException) {
                if(!isListening) {
                    closeDownThread()
                }
            } catch (e : SocketException) {
                println("Closing down thread due to error in main listener")
                closeDownThread()
            }
        }
    }
}

class ServerSideNetworkIncomingMessageHandler(clientSocket: Socket,
                                              incomingMessageQueue: BlockingQueue<String>) : Thread() {
    val POISON_STRING = "POISON_STRING"

    val messageQueue = incomingMessageQueue
    val clientSocket = clientSocket
    var isAddingMessages = true
    var reader : BufferedReader? = null

    override fun run() {
        reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        println("Starting incoming handler")
        addIncomingMessagesToQueue()
    }

    fun stopThreadExternally() {
        isAddingMessages = false
        this.interrupt()
    }

    fun closeDownThread() {
        isAddingMessages = false
        messageQueue.put(POISON_STRING)
    }

    fun addIncomingMessagesToQueue() {
        while (isAddingMessages) {
            try {
                var message : String? = reader?.readLine()
                if (message == null) {
                    messageQueue.put(POISON_STRING)
                    isAddingMessages = false
                    clientSocket.close()
                } else {
                    messageQueue.put(message)
                }
            } catch (e : InterruptedException) {
                if (!isAddingMessages) {
                    println("Interrupted externally")
                    closeDownThread()
                }
            } catch (e : SocketException) {
                println("Client connection lost")
                closeDownThread()
            }
        }
    }
}

class ServerSideNetworkOutgoingMessageHandler(clientSocket: Socket,
                                              outgoingMessageQueue: BlockingQueue<String>) : Thread() {
    val outgoingMessageQueue = outgoingMessageQueue
    val clientSocket = clientSocket
    var writer : PrintWriter? = null
    var isAddingMessages = true
    val POISON_STRING = "POISON_STRING"

    override fun run() {
        writer = PrintWriter(clientSocket.getOutputStream(), true)
        println("Starting outgoing handler")
        addOutgoingMessagesFromQueue()
    }

    fun stopThreadExternally() {
        isAddingMessages = false
        this.interrupt()
    }

    fun closeDownThread() {
        isAddingMessages = false
    }

    fun addOutgoingMessagesFromQueue() {
        while (isAddingMessages) {
            try {
                println("trying to take next message")
                var message: String = outgoingMessageQueue.take()
                if (message == POISON_STRING) {
                    isAddingMessages = false
                } else {
                    println("writing message: " + message)
                    writer?.write(message)
                    writer?.flush()
                    println("Wrote message")
                }
            } catch (e: InterruptedException) {
                if (!isAddingMessages) {
                    println("Interrupted externally")
                    closeDownThread()
                }
            } catch (e: SocketException) {
                println("Nothing from which to take message, closing outgoing")
                isAddingMessages = false
            }
        }
    }
}

fun main(args : Array<String>) {
    val network = ServerSideNetwork(1234, 0)
    network.setupServerAndStartListening()
}