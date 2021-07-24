Proof of concept for idea of using ktor-client on iOS with WebSockets by wrapping
of `IosClientEngine`.

Sample app connect to websockets on both platforms using ktor-client `WebSockets` feature:

```kotlin
val httpClient = HttpClient(createHttpClientEngine()) {
    install(WebSockets)
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
```

On android side it works out of box with `OkHttpEngine`. For iOS was implemented `HttpClientEngine`
wrapper - `WSIosHttpClientEngine`. To create this engine we should pass another engine that will
execute normal http requests:

```kotlin
WSIosHttpClientEngine(wrappedEngine = Ios.create { })
```

For details see classes: `WSIosHttpClientEngine`, `IosWebSocket`.
