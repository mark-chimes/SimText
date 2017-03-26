package client.base
import com.nhaarman.mockito_kotlin.*
import junit.framework.TestCase.*
import main.java.client.base.CommandConsumer
import main.java.network.NetworkConnection
import main.java.network.NetworkPacket
import main.java.network.NetworkPacketProducer
import main.java.server.Command
import main.java.server.CommandProducer
import main.java.server.Server
import org.junit.jupiter.api.*


/**
 * Created by Mark Chimes on 2017/02/19.
 */
class ServerTest {
    lateinit var server : Server

    @BeforeEach fun setupServer() {
        // server = Server()
    }

    @DisplayName("When the server has a prebuilt queue of messages")
    @Nested class queueSetUpTests {


        @Test fun serverCanInterpretShutdownCommandTest() {
            val commandProducer : CommandProducer = mock()
            whenever(commandProducer.getNextCommand()).thenReturn(Command("exit"))



        }
    }
}