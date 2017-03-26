package main.java.server

/**
 * Created by Mark Chimes on 2017/03/16.
 */
interface CommandProducer {
    fun getNextCommand() : Command
}