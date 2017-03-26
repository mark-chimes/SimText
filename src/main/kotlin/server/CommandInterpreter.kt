package main.java.server

/**
 * Created by Mark Chimes on 2017/03/20.
 */
class CommandInterpreter(commandProducer: CommandProducer) {
    val commandProducer = commandProducer

    fun executeNextCommand() {
        val command = commandProducer.getNextCommand()
        if (true) { // command type is exit

        }
    }
}