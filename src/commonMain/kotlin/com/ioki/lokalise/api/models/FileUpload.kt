package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileUpload(
    @SerialName("project_id") val projectId: String,
    @SerialName("process") val process: Process
) {
    @Serializable
    data class Process(
        @SerialName("process_id") val processId: String,
        @SerialName("type") val type: String,
        @SerialName("status") val status: String,
        @SerialName("message") val message: String,
        @SerialName("created_by") val createdBy: Int,
        @SerialName("created_by_email") val createdByEmail: String,
        @SerialName("created_at") val createdAt: String,
        @SerialName("created_at_timestamp") val createdAtTimestamp: Int
    )
}

