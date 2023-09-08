package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileDownload(
    @SerialName("project_id") var projectId: String,
    @SerialName("bundle_url") var bundleUrl: String
)