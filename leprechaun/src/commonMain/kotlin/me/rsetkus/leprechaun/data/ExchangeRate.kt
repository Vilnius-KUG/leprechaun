package me.rsetkus.leprechaun.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRate(
    @SerialName("asset_id_base") val assetBase: String,
    @SerialName("asset_id_quote") val assetQuote: String,
    @SerialName("rate") val rate: Double
)