package ru.alex009.samples.ktor.websockets

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }

    fun testRequest() {
        val httpClient = HttpClient(createHttpClientEngine()) {
            install(WebSockets) {
                pingInterval = 1000L
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            log("try connect websocket")

            httpClient.webSocket("wss://echo.websocket.org") {
                log("connected websocket")

                val incomingJob = launch {
                    incoming.consumeEach { frame ->
                        log(frame.toString())

                        if (frame is Frame.Text) {
                            val text: String = frame.readText()
                            log("received $text")

                            outgoing.send(Frame.Text(">$text"))
                            log("send response")
                        }
                    }
                }
                send("Hello world!")
                log("send first message")

                incomingJob.join()
                log("incoming job end")
            }

            log("websocket closed")
        }
    }
}

expect fun createHttpClientEngine(): HttpClientEngine

expect fun log(message: String)