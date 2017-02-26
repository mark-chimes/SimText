package client.gui

import client.base.Client
import javax.swing.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_ENTER
import java.awt.event.KeyListener
import org.mockito.ArgumentCaptor
import java.awt.GridBagConstraints
import javax.swing.JButton





/**
 * Created by Mark Chimes on 2017/02/19.
 */
class Gui {
    val guiClient: Client
    lateinit var frame : JFrame ; private set

    val MAIN_WINDOW_NAME = "mainWindow"
    val SEND_BUTTON_NAME = "sendButton"
    val INPUT_TEXT_FIELD_NAME = "inputTextField"
    val OUTPUT_TEXT_AREA_NAME = "outputTextArea"



    var isStarted : Boolean = false ; private set

    constructor(client : Client) {
        this.guiClient = client
        makeGui()
    }

    fun makeGui() {
        frame = JFrame()
        frame.name = MAIN_WINDOW_NAME

        val frameLayout = GridBagLayout()
        frame.layout = frameLayout

        val outputTextArea = JTextArea("Output text area \n My output text area")
        outputTextArea.name = OUTPUT_TEXT_AREA_NAME
        outputTextArea.isEditable = false
        val outputTextAreaConstraints = GridBagConstraints()
        outputTextAreaConstraints.fill = GridBagConstraints.HORIZONTAL
        outputTextAreaConstraints.gridx = 0
        outputTextAreaConstraints.gridy = 0
        outputTextAreaConstraints.weightx = 1.0
        outputTextAreaConstraints.weighty = 0.8
        outputTextAreaConstraints.ipady = 1000
        frame.add(outputTextArea, outputTextAreaConstraints)

        val inputTextField = InputTextField(initialContent="Input Text Field", name=INPUT_TEXT_FIELD_NAME)
        val inputTextFieldConstraints = GridBagConstraints()
        inputTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL
        inputTextFieldConstraints.gridx = 0
        inputTextFieldConstraints.gridy = 1
        frame.add(inputTextField.textField, inputTextFieldConstraints)


        val sendButton = JButton("send")
        sendButton.name = SEND_BUTTON_NAME
        sendButton.toolTipText = "Send command"
        sendButton.addActionListener { inputTextField.sendContainingText() }
        val sendButtonConstraints = GridBagConstraints()
        sendButtonConstraints.fill = GridBagConstraints.HORIZONTAL
        sendButtonConstraints.gridx = 0
        sendButtonConstraints.gridy = 2
        frame.add(sendButton, sendButtonConstraints)
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        frame.setSize(400, 500)
        frame.isVisible = true

        isStarted = true
    }


    fun killGui() {
        frame.dispose()
    }

    inner class InputTextField(initialContent: String = "", name : String) {
        val textField : JTextField = JTextField(initialContent)

        init {
            textField.name = name
            addKeyListener()
        }

        fun addKeyListener() {
            textField.addKeyListener(
                    object : KeyListener {
                        override fun keyTyped(e: KeyEvent?) = Unit

                        override fun keyPressed(e: KeyEvent?) {
                            if (e != null) {
                                if(e.keyCode == VK_ENTER) {
                                    sendContainingText()
                                }
                            }
                        }
                        override fun keyReleased(e: KeyEvent?) = Unit
                    })
        }

        fun sendContainingText() {
            guiClient.sendMessage(textField.text);
            textField.text = ""
        }
    }
}

fun main(args : Array<String>) {
    val client =  PrintingClient()
    Gui(client)
}

class PrintingClient : Client() {
    override fun sendMessage(message: String) {
        println(message)
    }
}

