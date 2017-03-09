package main.java.client.base

/**
 * Created by Mark Chimes on 2017/03/08.
 */
interface DisplayMessageProducer {
    fun pullDisplayMessages() : Array<String>
}