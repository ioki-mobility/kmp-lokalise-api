package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileDownload(
    @SerialName("project_id") val projectId: String,
    @SerialName("bundle_url") val bundleUrl: String
)