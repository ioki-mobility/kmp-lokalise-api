package  com.ioki.lokalise.api.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DownloadFilesRequestBody(
    @SerialName("format")
    val format: String,
    @SerialName("original_filenames")
    val originalFilenames: Boolean? = null,
    @SerialName("bundle_structure")
    val bundleStructure: String? = null,
    @SerialName("directory_prefix")
    val directoryPrefix: String? = null,
    @SerialName("all_platforms")
    val allPlatforms: Boolean? = null,
    @SerialName("filter_langs")
    val filterLangs: List<String>? = null,
    @SerialName("filter_data")
    val filterData: List<String>? = null,
    @SerialName("filter_filenames")
    val filterFilenames: List<String>? = null,
    @SerialName("add_newline_eof")
    val addNewlineEof: Boolean? = null,
    @SerialName("custom_translation_status_ids")
    val customTranslationStatusIds: List<String>? = null,
    @SerialName("include_tags")
    val includeTags: List<String>? = null,
    @SerialName("exclude_tags")
    val excludeTags: List<String>? = null,
    @SerialName("export_sort")
    val exportSort: String? = null,
    @SerialName("export_empty_as")
    val exportEmptyAs: String? = null,
    @SerialName("export_null_as")
    val exportNullAs: String? = null,
    @SerialName("include_comments")
    val includeComments: Boolean? = null,
    @SerialName("include_description")
    val includeDescription: Boolean? = null,
    @SerialName("include_pids")
    val includePids: List<String>? = null,
    @SerialName("triggers")
    val triggers: List<String>? = null,
    @SerialName("filter_repositories")
    val filterRepositories: List<String>? = null,
    @SerialName("replace_breaks")
    val replaceBreaks: Boolean? = null,
    @SerialName("disable_references")
    val disableReferences: Boolean? = null,
    @SerialName("plural_format")
    val pluralFormat: String? = null,
    @SerialName("placeholder_format")
    val placeholderFormat: String? = null,
    @SerialName("webhook_url")
    val webhookUrl: String? = null,
    @SerialName("language_mapping")
    val languageMapping: List<String>? = null,
    @SerialName("icu_numeric")
    val icuNumeric: Boolean? = null,
    @SerialName("escape_percent")
    val escapePercent: Boolean? = null,
    @SerialName("indentation")
    val indentation: String? = null,
    @SerialName("yaml_include_root")
    val yamlIncludeRoot: Boolean? = null,
    @SerialName("json_unescaped_slashes")
    val jsonUnescapedSlashes: Boolean? = null,
    @SerialName("java_properties_encoding")
    val javaPropertiesEncoding: String? = null,
    @SerialName("java_properties_separator")
    val javaPropertiesSeparator: String? = null,
    @SerialName("bundle_description")
    val bundleDescription: String? = null,
    @SerialName("filter_project_id")
    val filterProjectId: String? = null,
    @SerialName("compact")
    val compact: Boolean? = null
)
