package tests

import main.java.client.base.CommandConsumer

class GuiRunner {
    val client: CommandConsumer

    constructor(client: CommandConsumer) {
        this.client = client
    }

    fun guiIsRunning(): Boolean {
        return true
    }

    fun startGame() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}





