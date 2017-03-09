package client.base

import com.sun.xml.internal.fastinfoset.util.StringArray
import main.java.client.base.*
import main.java.network.NetworkPacket
import main.java.network.NetworkPacketProducer

/**
 * Created by Mark Chimes on 2017/02/19.
 */
class Client(inServerComms: NetworkPacketProducer, outServerComms : CommandConsumer) : CommandConsumer, DisplayMessageProducer {
    val inServerComms = inServerComms
    val outServerComms = outServerComms

    override fun pullDisplayMessages(): Array<String> {
        val returnMessages : NetworkPacket = inServerComms.pullNextPacketFromQueue()
        return arrayOf(returnMessages.message)
    }

    override fun sendMessage(message : String) {
        outServerComms.sendMessage(message)
    }
}
