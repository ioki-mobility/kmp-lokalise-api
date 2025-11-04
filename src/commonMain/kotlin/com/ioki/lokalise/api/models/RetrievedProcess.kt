package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
data class RetrievedProcess(
    @SerialName("process") val process: Process
)

@Serializable
sealed interface Process {
    val processId: String
    val type: String
    val status: String
    val message: String?
    val createdBy: Int
    val createdAt: String
    val createdAtTimestamp: Long

    @Serializable
    @SerialName("file-import")
    data class FileUpload(
        @SerialName("process_id") override val processId: String,
        override val type: String,
        override val status: String,
        override val message: String?,
        @SerialName("created_by") override val createdBy: Int,
        @SerialName("created_at") override val createdAt: String,
        @SerialName("created_at_timestamp") override val createdAtTimestamp: Long,
        @SerialName("created_by_email") val createdByEmail: String,
        val details: FileUploadDetails
    ) : Process

    @Serializable
    @SerialName("async-export")
    data class AsyncExport(
        @SerialName("process_id") override val processId: String,
        override val type: String,
        override val status: String,
        override val message: String?,
        @SerialName("created_by") override val createdBy: Int,
        @SerialName("created_at") override val createdAt: String,
        @SerialName("created_at_timestamp") override val createdAtTimestamp: Long,
        val details: AsyncExportDetails?
    ) : Process
}

@Serializable
data class FileUploadDetails(
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

@Serializable(with = AsyncExportDetailsSerializer::class)
sealed interface AsyncExportDetails {
    @Serializable
    data class Running(
        @SerialName("items_to_process") val itemsToProcess: Int,
        @SerialName("items_processed") val itemsProcessed: Int
    ) : AsyncExportDetails

    @Serializable
    data class Finished(
        @SerialName("file_size_kb") val fileSizeKb: Int,
        @SerialName("total_number_of_keys") val totalNumberOfKeys: Int,
        @SerialName("download_url") val downloadUrl: String
    ) : AsyncExportDetails
}

internal object AsyncExportDetailsSerializer :
    JsonContentPolymorphicSerializer<AsyncExportDetails>(AsyncExportDetails::class) {
    override fun selectDeserializer(element: JsonElement) = when {
        element.jsonObject.containsKey("download_url") -> AsyncExportDetails.Finished.serializer()
        element.jsonObject.containsKey("items_to_process") -> AsyncExportDetails.Running.serializer()
        else -> throw SerializationException("Unknown AsyncExportDetails type")
    }
}