package me.rsetkus.leprechaun

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import me.rsetkus.leprechaun.data.ExchangeRate
import me.rsetkus.leprechaun.util.KotlinNativeFlowWrapper
import me.setkus.leprechaun.config.BuildKonfig.API_KEY
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class CryptoRepository {

    private val baseUrl = "https://rest.coinapi.io/v1"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    init {
        client.sendPipeline.intercept(HttpSendPipeline.State) {
            context.headers.append("X-CoinAPI-Key", API_KEY)
        }
    }

    fun getExchangeRate(base: String) = KotlinNativeFlowWrapper<ExchangeRate>(tickerFlow(2.seconds) {
        client.get("$baseUrl/exchangerate/$base/USD").body()
    })
}

private fun <T> tickerFlow(period: Duration, action: suspend () -> T): Flow<T>  = flow {
    while (true) {
        emit(action())
        delay(period)
    }
}