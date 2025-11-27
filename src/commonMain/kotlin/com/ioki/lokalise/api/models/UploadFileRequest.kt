package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadFileRequest(
    @SerialName("data")
    val data: String,
    @SerialName("filename")
    val filename: String,
    @SerialName("lang_iso")
    val langIso: String,
    @SerialName("convert_placeholders")
    val convertPlaceholders: Boolean? = null,
    @SerialName("detect_icu_plurals")
    val detectIcuPlurals: Boolean? = null,
    @SerialName("tags")
    val tags: List<String>? = null,
    @SerialName("tag_inserted_keys")
    val tagInsertedKeys: Boolean? = null,
    @SerialName("tag_updated_keys")
    val tagUpdatedKeys: Boolean? = null,
    @SerialName("tag_skipped_keys")
    val tagSkippedKeys: Boolean? = null,
    @SerialName("replace_modified")
    val replaceModified: Boolean? = null,
    @SerialName("slashn_to_linebreak")
    val slashnToLinebreak: Boolean? = null,
    @SerialName("keys_to_values")
    val keysToValues: Boolean? = null,
    @SerialName("distinguish_by_file")
    val distinguishByFile: Boolean? = null,
    @SerialName("apply_tm")
    val applyTm: Boolean? = null,
    @SerialName("use_automations")
    val useAutomations: Boolean? = null,
    @SerialName("hidden_from_contributors")
    val hiddenFromContributors: Boolean? = null,
    @SerialName("cleanup_mode")
    val cleanupMode: Boolean? = null,
    @SerialName("custom_translation_status_ids")
    val customTranslationStatusIds: List<String>? = null,
    @SerialName("custom_translation_status_inserted_keys")
    val customTranslationStatusInsertedKeys: Boolean? = null,
    @SerialName("custom_translation_status_updated_keys")
    val customTranslationStatusUpdatedKeys: Boolean? = null,
    @SerialName("custom_translation_status_skipped_keys")
    val customTranslationStatusSkippedKeys: Boolean? = null,
    @SerialName("skip_detect_lang_iso")
    val skipDetectLangIso: Boolean? = null,
    @SerialName("format")
    val format: String? = null,
    @SerialName("filter_task_id")
    val filterTaskId: Int? = null,
)
