package client.gui

import client.base.Client
import javax.swing.*
import com.nhaarman.mockito_kotlin.mock
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_ENTER
import java.awt.event.KeyListener

/**
 * Created by Mark Chimes on 2017/02/19.
 */
class Gui {
    val client : Client
    lateinit var frame : JFrame ; private set

    val MAIN_WINDOW_NAME = "mainWindow"
    val SEND_BUTTON_NAME = "sendButton"
    val INPUT_TEXT_FIELD_NAME = "inputTextField"
    val OUTPUT_TEXT_AREA_NAME = "outputTextArea"


    lateinit var sendButton : JButton

    var isStarted : Boolean = false ; private set

    constructor(client : Client) {
        this.client = client
        makeGui()
    }

    fun makeGui() {
        frame = JFrame()
        frame.name = MAIN_WINDOW_NAME

        val outputTextArea = JTextArea("Output text area")
        outputTextArea.name = OUTPUT_TEXT_AREA_NAME
        outputTextArea.isEditable = false

        val inputTextField = JTextField("Input text field")
        inputTextField.name = INPUT_TEXT_FIELD_NAME

        inputTextField.addKeyListener(
                object : KeyListener {
            override fun keyTyped(e: KeyEvent?) = Unit

            override fun keyPressed(e: KeyEvent?) {
                if (e != null) {
                    if(e.keyCode == KeyEvent.VK_ENTER) {
                        println("Enter key pressed!")
                        client.sendMessage(inputTextField.text);
                    }
                }
            }

            override fun keyReleased(e: KeyEvent?) = Unit

        })

        sendButton = JButton("send")
        sendButton.name = SEND_BUTTON_NAME
        sendButton.toolTipText = "Send command"
        sendButton.addActionListener(
                ActionListener {
                    fun actionPerformed(e: ActionEvent) {
                        print("Print action")
                    }
                })

        frame.add(outputTextArea)
        frame.add(inputTextField)
        frame.add(sendButton)

        frame.setSize(400, 500)
        frame.layout = GridLayout(3,1)


        frame.isVisible = true

        isStarted = true
    }


    fun killGui() {
        frame.dispose()
    }

}

fun main(args : Array<String>) {
    val mockClient : Client = mock()
    Gui(mockClient)
}