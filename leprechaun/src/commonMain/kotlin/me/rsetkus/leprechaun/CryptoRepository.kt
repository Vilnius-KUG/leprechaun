package me.rsetkus.leprechaun

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import lt.setkus.leprechaun.config.BuildKonfig.API_KEY
import me.rsetkus.leprechaun.data.ExchangeRate

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

    suspend fun getExchangeRate(base: String): ExchangeRate = client.get("$baseUrl/exchangerate/$base/USD").body()
}