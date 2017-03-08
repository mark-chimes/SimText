package client.gui

import javax.swing.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import java.awt.*
import java.awt.event.KeyEvent.VK_ENTER
import org.mockito.ArgumentCaptor
import java.awt.GridBagConstraints
import javax.swing.JButton
import com.sun.awt.SecurityWarning.getSize
import main.java.client.base.CommandConsumer
import javax.swing.Spring.height
import javax.swing.Spring.width
import java.awt.Toolkit.getDefaultToolkit
import java.awt.Dimension
import java.awt.event.*
import javax.swing.ImageIcon


/**
 * Created by Mark Chimes on 2017/02/19.
 */
class Gui(inputReceiver: CommandConsumer) {
    val inputReceiver: CommandConsumer = inputReceiver
    lateinit var frame: JFrame; private set

    val MAIN_WINDOW_NAME = "mainWindow"
    val SEND_BUTTON_NAME = "sendButton"
    val INPUT_TEXT_FIELD_NAME = "inputTextField"
    val OUTPUT_TEXT_AREA_NAME = "outputTextArea"
    val outputTextArea = JTextArea("")

    init {
        makeGui()
    }

    var isStarted : Boolean = false ; private set

    fun makeGui() {
        frame = JFrame()
        frame.name = MAIN_WINDOW_NAME

        val frameLayout = GridBagLayout()
        frame.layout = frameLayout

        outputTextArea.name = OUTPUT_TEXT_AREA_NAME
        outputTextArea.isEditable = false
        outputTextArea.caret.isSelectionVisible = true
        val outputTextAreaConstraints = GridBagConstraints()
        outputTextAreaConstraints.fill = GridBagConstraints.HORIZONTAL
        outputTextAreaConstraints.gridx = 0
        outputTextAreaConstraints.gridy = 0
        outputTextAreaConstraints.weightx = 1.0
        outputTextAreaConstraints.weighty = 0.8
        outputTextAreaConstraints.ipady = 1000
        frame.add(outputTextArea, outputTextAreaConstraints)

        val inputTextField = InputTextField(initialContent="", name=INPUT_TEXT_FIELD_NAME)
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

        frame.setSize(800, 600)
        frame.setLocationRelativeTo(null)

        val img = ImageIcon("icons/three-people-glasses-middle.png", "game icon")
        frame.iconImage = img.image
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
            sendMessageOnEnterPressed()
            ensureTextBoxIsFocussed()
        }

        fun sendMessageOnEnterPressed() {
            textField.addKeyListener(
                    object : KeyListener {
                        override fun keyTyped(e: KeyEvent?) = Unit

                        override fun keyPressed(e: KeyEvent?) {
                            if (e != null) {
                                if (e.keyCode == VK_ENTER) {
                                    sendContainingText()
                                }
                            }
                        }

                        override fun keyReleased(e: KeyEvent?) = Unit
                    })
        }

        fun ensureTextBoxIsFocussed() {
            frame.addWindowListener(
                    object : WindowAdapter() {
                        override fun windowOpened(e : WindowEvent){
                            textField.requestFocus()
                }
            })
        }


        fun sendContainingText() {
            val newText : String = textField.text
            if (newText != "") {
                inputReceiver.sendMessage(newText)
                outputTextArea.append(newText + "\n")
                textField.text = ""
            }
        }


    }
}
