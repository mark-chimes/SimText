package main.java.network

/**
 * Created by Mark Chimes on 2017/03/09.
 */
data class NetworkPacket(val message : String)

interface NetworkPacketProducer {
    fun pullNextPacketFromQueue() : NetworkPacket
}