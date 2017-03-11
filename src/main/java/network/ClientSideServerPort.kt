package main.java.network

import java.net.Socket
import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil.getOutputStream
import java.io.PrintWriter




/**
 * Created by Mark Chimes on 2017/03/11.
 */

class ClientSideServerPort() {

    fun connectToHost() {

    }

}

fun main(args : Array<String>) {
    val socket = Socket("127.0.0.1", 1234)
    print("Connected: " + socket.isConnected)
    val out = PrintWriter(socket.getOutputStream(), true)
    out.write("Potato")
    socket.close()
}