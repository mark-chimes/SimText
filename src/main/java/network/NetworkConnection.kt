package main.java.network

import main.java.client.base.CommandConsumer

/**
 * Created by Mark Chimes on 2017/03/09.
 */
class NetworkConnection : NetworkPacketProducer, CommandConsumer {
    override fun pullNextPacketFromQueue(): NetworkPacket {
        TODO("not implemented")
    }

    override fun sendMessage(message: String) {
        TODO("not implemented")
    }

}