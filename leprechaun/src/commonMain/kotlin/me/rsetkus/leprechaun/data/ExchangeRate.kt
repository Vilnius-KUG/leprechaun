package me.rsetkus.leprechaun.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.rsetkus.leprechaun.domain.ExchangeRateDomain
import kotlin.math.round

@Serializable
data class ExchangeRate(
    @SerialName("asset_id_base") val assetBase: String,
    @SerialName("asset_id_quote") val assetQuote: String,
    @SerialName("rate") val rate: Double
)

fun ExchangeRate.toDomain()  = ExchangeRateDomain(round(this.rate))