package tests

import client.base.Client

class GuiRunner {
    val client : Client

    constructor(client: Client) {
        this.client = client
    }

    fun  guiIsRunning(): Boolean {
        return true
    }

    fun startGame() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}





