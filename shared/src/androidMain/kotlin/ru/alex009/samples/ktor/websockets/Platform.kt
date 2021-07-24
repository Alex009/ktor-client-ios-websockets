package ru.alex009.samples.ktor.websockets

import android.util.Log
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun createHttpClientEngine(): HttpClientEngine {
    return OkHttp.create {

    }
}

actual fun log(message: String) {
    Log.d("KCWS", message)
}
