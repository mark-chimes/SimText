package client.base
import com.nhaarman.mockito_kotlin.*
import main.java.client.base.CommandConsumer
import main.java.network.NetworkConnection
import main.java.network.NetworkPacket
import main.java.network.NetworkPacketProducer
import org.junit.jupiter.api.*


/**
 * Created by Mark Chimes on 2017/02/19.
 */
class ClientTest {
    lateinit var client : Client
    lateinit var fakeInServerComms: NetworkPacketProducer
    lateinit var fakeOutServerComms: CommandConsumer


    @BeforeEach fun setupClient() {
        fakeInServerComms = mock()
        fakeOutServerComms = mock()
        client = Client(fakeInServerComms, fakeOutServerComms)
    }

    @DisplayName("client can display a blank message")
    @Test fun clientCanDisplayBlankMessageTest() {
        val testMessage = ""
        whenever(fakeInServerComms.pullNextPacketFromQueue()).thenReturn(NetworkPacket(testMessage))

        val displayMessages = client.pullDisplayMessages()
        assertEquals("Wrong message received", testMessage, displayMessages[0])
    }

    @DisplayName("client can display a simple message")
    @Test fun clientCanDisplayMessageTest() {
        val testMessage = "Test Message 1"
        whenever(fakeInServerComms.pullNextPacketFromQueue()).thenReturn(NetworkPacket(testMessage))

        val displayMessages = client.pullDisplayMessages()
        assertEquals("Wrong message received", testMessage, displayMessages[0])
    }

    @DisplayName("client can send a blank message")
    @Test fun clientCanSendBlankMessageTest() {
        val testMessage = ""

        client.sendMessage(testMessage)

        verify(fakeOutServerComms, atLeastOnce()).sendMessage(testMessage)
    }

    @DisplayName("client can send a simple message")
    @Test fun clientCanSendSimpleMessageTest() {
        val testMessage = "Test Message 1"

        client.sendMessage(testMessage)

        verify(fakeOutServerComms, atLeastOnce()).sendMessage(testMessage)
    }
}

/*
Client should be able to:
 - connect to existing headless server
 - create its own server
 - handle basic commands like exiting
 */