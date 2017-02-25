package client.gui

import client.base.Client
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.TestCase.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.AfterEach
import java.awt.Component
import java.awt.Container
import java.awt.event.KeyEvent
import javax.swing.*

/**
 * Created by Mark Chimes on 2017/02/19.
 */

@DisplayName("After the gui has been set up")
class GuiTest {
    lateinit var gui : Gui
    lateinit var mockClient : Client
    lateinit var mainFrame : JFrame

    @BeforeEach fun setupGui()
    {
        mockClient = mock()
        gui = Gui(mockClient)
        mainFrame = gui.frame
    }

    @AfterEach fun breakDownGui()
    {
        gui.killGui()
    }

    @DisplayName("Has the GUI started?")
    @Test fun guiStartedTest() {
        assertTrue("Gui not started", gui.isStarted)
    }

    @DisplayName("Are the components present?")
    @Test fun buttonPresentTest() {
        assertNotNull("Window not present", getChildNamed(gui.MAIN_WINDOW_NAME))
        assertNotNull("Output text area not present", getChildNamed(gui.OUTPUT_TEXT_AREA_NAME))
        assertNotNull("Input text field not present", getChildNamed(gui.INPUT_TEXT_FIELD_NAME))
        assertNotNull("Send button not present", getChildNamed(gui.SEND_BUTTON_NAME))
    }

    @DisplayName("Can a word be typed in the input text field?")
    @Test fun typeWordInInputTextFieldTest() {
        typeMessage("potato")
        assertTrue("Cannot type word", textFieldContains("potato"))
    }

    @DisplayName("Can a message be sent?")
    @Test fun sendMessageTest() {
        typeMessage("Test Message 1")
        pressEnterOnTextBox()
        // com.nhaarman.mockito_kotlin.whenever(mockClient.sendMessage("Test Message 1")).then { com.nhaarman.mockito_kotlin.doReturn() }

        verify(mockClient).sendMessage("Test Message 1")
        clickSendButton()
    }

    private fun typeMessage(message : String) {
        val textField = getChildNamed(gui.INPUT_TEXT_FIELD_NAME, mainFrame)

        if (textField is JTextField) {
            textField.text = message
        }
    }

    private fun clickSendButton() {
        val button = getChildNamed(gui.SEND_BUTTON_NAME, mainFrame)

        if (button is JButton) {
            button.doClick()
        }
    }

    fun getChildNamed(name: String): Component {
        val component : Component? = getChildNamed(gui.MAIN_WINDOW_NAME, mainFrame)
        if (component == null) {
            throw Exception("Widget '${name}' null")
        } else {
            return component
        }
    }

    fun getChildNamed(name : String, container : Container) : Component? {
        if (name == container.name) {
            return container
        }
        for (c in container.components) {
            if (name == c.name) {
                return c
            } else if (c is Container) {
                var outComponent = getChildNamed(name, c)
                if (outComponent != null) {
                    return outComponent
                }
            }
        }
        return null
    }

    fun textFieldContains(message : String) : Boolean {
        val textField = getChildNamed(gui.INPUT_TEXT_FIELD_NAME, mainFrame)
        return (textField is JTextField) && (textField.text == message)
    }

    fun pressEnterOnTextBox() {
        val KEY_PRESS_TIME : Long = 10
        val NO_MODIFIERS : Int = 0
        val textField = getChildNamed(gui.INPUT_TEXT_FIELD_NAME, mainFrame)
        if (textField is JTextField) {
            val keyEventPressed = KeyEvent(textField, KeyEvent.KEY_PRESSED, KEY_PRESS_TIME, NO_MODIFIERS, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED)
            val keyEventReleased = KeyEvent(textField, KeyEvent.KEY_RELEASED, KEY_PRESS_TIME, NO_MODIFIERS, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED)

            textField.dispatchEvent(keyEventPressed)
            textField.dispatchEvent(keyEventReleased)

        }
    }
}
