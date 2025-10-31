package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RetrievedProcess(
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
        @SerialName("created_at_timestamp") val createdAtTimestamp: Int,
        @SerialName("download_url") val downloadUrl: String,
        @SerialName("details") val details: Details,
    ) {
        @Serializable
        data class Details(
            @SerialName("files") val files: List<Files>,
        ) {
            @Serializable
            data class Files(
                @SerialName("status") val status: String,
                @SerialName("message") val message: String,
                @SerialName("name_original") val nameOriginal: String,
                @SerialName("name_custom") val nameCustom: String?,
                @SerialName("word_count_total") val wordCountTotal: Int,
                @SerialName("key_count_total") val keyCountTotal: Int,
                @SerialName("key_count_inserted") val keyCountInserted: Int,
                @SerialName("key_count_updated") val keyCountUpdated: Int,
                @SerialName("key_count_skipped") val keyCountSkipped: Int,
            )
        }
    }
}