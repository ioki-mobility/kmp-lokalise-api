package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileUpload(
    @SerialName("project_id") var projectId: String,
    @SerialName("process") var process: Process
) {
    @Serializable
    data class Process(
        @SerialName("process_id") var processId: String,
        @SerialName("type") var type: String,
        @SerialName("status") var status: String,
        @SerialName("message") var message: String,
        @SerialName("created_by") var createdBy: Int,
        @SerialName("created_by_email") var createdByEmail: String,
        @SerialName("created_at") var createdAt: String,
        @SerialName("created_at_timestamp") var createdAtTimestamp: Int
    )
}

