package main.java.network

import main.java.client.base.CommandConsumer

/**
 * Created by Mark Chimes on 2017/03/09.
 */
class NetworkConnection : NetworkPacketProducer, NetworkPacketConsumer, CommandConsumer {
    

    override fun sendPacket(packet: NetworkPacket) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pullNextPacketFromQueue(): NetworkPacket {
        TODO("not implemented")
    }

    override fun sendMessage(message: String) {
        TODO("not implemented")
    }

}

data class NetworkPacket(val message: String)

interface NetworkPacketProducer {
    fun pullNextPacketFromQueue() : NetworkPacket
}

interface NetworkPacketConsumer {
    fun sendPacket(packet : NetworkPacket)
}