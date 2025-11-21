package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DownloadFilesAsyncResponse(@SerialName("process_id") val processId: String)
