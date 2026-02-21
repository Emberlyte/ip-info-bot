package com.github.emberlyte.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class IpClass(
    val ip: String,
    val country: String,
    @SerialName("country_code") val countryCode: String,
    @SerialName("as_name") val asName: String,
    val continent: String,
    @SerialName("continent_code") val continentCode: String,
    val asn: String,
    @SerialName("as_domain") val asDomain: String
)
