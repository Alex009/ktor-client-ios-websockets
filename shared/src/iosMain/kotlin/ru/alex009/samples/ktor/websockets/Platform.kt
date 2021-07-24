package ru.alex009.samples.ktor.websockets

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.ios.Ios
import platform.Foundation.NSLog
import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val platform: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun createHttpClientEngine(): HttpClientEngine {
    return Ios.create {

    }
}

actual fun log(message: String) {
    NSLog(message)
}
