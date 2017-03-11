package main.java.network

import java.net.Socket
import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil.getOutputStream
import java.io.PrintWriter




/**
 * Created by Mark Chimes on 2017/03/11.
 */

class ClientSideNetwork() {

    fun connectToHost() {

    }

}

fun main(args : Array<String>) {
    val socket = Socket("10.0.0.174", 1234)
    println("Connected: " + socket.isConnected)
    val out = PrintWriter(socket.getOutputStream(), true)

    for (i in 1..6) {
        println("Writing: Potato: " + socket.localPort + " " + i)
        out.write("Potato: " + socket.localPort + " " + i + "\n")
        out.flush()
        Thread.sleep(1000)
    }
    socket.close()
}