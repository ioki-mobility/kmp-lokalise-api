package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorWrapper(
    val error: Error,
)

@Serializable
data class Error(
    @SerialName("message") val message: String,
    @SerialName("code") val code: Int
)