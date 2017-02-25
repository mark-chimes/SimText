package tests

import client.base.Client

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EndToEndTest {

	lateinit var guiBot : GuiRunner

	@Before
	fun startUp() {
		val client = Client()
		guiBot = GuiRunner(client)

		Assert.assertTrue(guiBot.guiIsRunning())
	}

	@Test
	fun startGameTest() {
		guiBot.startGame()
	}

	@After
	fun cleanUp() {
		// TODO
	}
}


