package main.java.client.base

/**
 * Created by Mark Chimes on 2017/03/02.
 */
interface CommandConsumer {
    fun sendMessage(message: String)
}