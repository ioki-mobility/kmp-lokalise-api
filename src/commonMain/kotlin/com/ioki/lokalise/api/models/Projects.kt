package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Projects(
    @SerialName("projects") val projects: List<Project>
)

@Serializable
data class Project(
    @SerialName("project_id") val projectId: String,
    @SerialName("project_type") val projectType: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("created_at_timestamp") val createdAtTimestamp: Int,
    @SerialName("created_by") val createdBy: Int,
    @SerialName("created_by_email") val createdByEmail: String,
    @SerialName("team_id") val teamId: Int,
    @SerialName("base_language_id") val baseLanguageId: Int,
    @SerialName("base_language_iso") val baseLanguageIso: String,
    @SerialName("settings") val settings: Settings,
    @SerialName("statistics") val statistics: Statistics
) {
    @Serializable
    data class Settings(
        @SerialName("per_platform_key_names") val perPlatformKeyNames: Boolean,
        @SerialName("reviewing") val reviewing: Boolean,
        @SerialName("auto_toggle_unverified") val autoToggleUnverified: Boolean,
        @SerialName("offline_translation") val offlineTranslation: Boolean,
        @SerialName("key_editing") val keyEditing: Boolean,
        @SerialName("inline_machine_translations") val inlineMachineTranslations: Boolean,
        @SerialName("branching") val branching: Boolean,
        @SerialName("segmentation") val segmentation: Boolean,
        @SerialName("custom_translation_statuses") val customTranslationStatuses: Boolean,
        @SerialName("custom_translation_statuses_allow_multiple") val customTranslationStatusesAllowMultiple: Boolean,
        @SerialName("contributor_preview_download_enabled") val contributorPreviewDownloadEnabled: Boolean
    )

    @Serializable
    data class Statistics(
        @SerialName("progress_total") val progressTotal: Int,
        @SerialName("keys_total") val keysTotal: Int,
        @SerialName("team") val team: Int,
        @SerialName("base_words") val baseWords: Int,
        @SerialName("qa_issues_total") val qaIssuesTotal: Int,
        @SerialName("qa_issues") val qaIssues: QaIssues,
        @SerialName("languages") val languages: List<Languages>
    ) {
        @Serializable
        data class QaIssues(
            @SerialName("not_reviewed") val notReviewed: Int,
            @SerialName("unverified") val unverified: Int,
            @SerialName("spelling_grammar") val spellingGrammar: Int,
            @SerialName("inconsistent_placeholders") val inconsistentPlaceholders: Int,
            @SerialName("inconsistent_html") val inconsistentHtml: Int,
            @SerialName("different_number_of_urls") val differentNumberOfUrls: Int,
            @SerialName("different_urls") val differentUrls: Int,
            @SerialName("leading_whitespace") val leadingWhitespace: Int,
            @SerialName("trailing_whitespace") val trailingWhitespace: Int,
            @SerialName("different_number_of_email_address") val differentNumberOfEmailAddress: Int,
            @SerialName("different_email_address") val differentEmailAddress: Int,
            @SerialName("different_brackets") val differentBrackets: Int,
            @SerialName("different_numbers") val differentNumbers: Int,
            @SerialName("double_space") val doubleSpace: Int,
            @SerialName("special_placeholder") val specialPlaceholder: Int,
            @SerialName("unbalanced_brackets") val unbalancedBrackets: Int

        )

        @Serializable
        data class Languages(
            @SerialName("language_id") val languageId: Int,
            @SerialName("language_iso") val languageIso: String,
            @SerialName("progress") val progress: Int,
            @SerialName("words_to_do") val wordsToDo: Int
        )
    }
}