package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Projects(
    @SerialName("projects") val projects: List<Project>
)

@Serializable
data class Project(
    @SerialName("project_id") var projectId: String,
    @SerialName("project_type") var projectType: String,
    @SerialName("name") var name: String,
    @SerialName("description") var description: String,
    @SerialName("created_at") var createdAt: String,
    @SerialName("created_at_timestamp") var createdAtTimestamp: Int,
    @SerialName("created_by") var createdBy: Int,
    @SerialName("created_by_email") var createdByEmail: String,
    @SerialName("team_id") var teamId: Int,
    @SerialName("base_language_id") var baseLanguageId: Int,
    @SerialName("base_language_iso") var baseLanguageIso: String,
    @SerialName("settings") var settings: Settings,
    @SerialName("statistics") var statistics: Statistics
) {
    @Serializable
    data class Settings(
        @SerialName("per_platform_key_names") var perPlatformKeyNames: Boolean,
        @SerialName("reviewing") var reviewing: Boolean,
        @SerialName("auto_toggle_unverified") var autoToggleUnverified: Boolean,
        @SerialName("offline_translation") var offlineTranslation: Boolean,
        @SerialName("key_editing") var keyEditing: Boolean,
        @SerialName("inline_machine_translations") var inlineMachineTranslations: Boolean,
        @SerialName("branching") var branching: Boolean,
        @SerialName("segmentation") var segmentation: Boolean,
        @SerialName("custom_translation_statuses") var customTranslationStatuses: Boolean,
        @SerialName("custom_translation_statuses_allow_multiple") var customTranslationStatusesAllowMultiple: Boolean,
        @SerialName("contributor_preview_download_enabled") var contributorPreviewDownloadEnabled: Boolean
    )

    @Serializable
    data class Statistics(
        @SerialName("progress_total") var progressTotal: Int,
        @SerialName("keys_total") var keysTotal: Int,
        @SerialName("team") var team: Int,
        @SerialName("base_words") var baseWords: Int,
        @SerialName("qa_issues_total") var qaIssuesTotal: Int,
        @SerialName("qa_issues") var qaIssues: QaIssues,
        @SerialName("languages") var languages: List<Languages>
    ) {
        @Serializable
        data class QaIssues(
            @SerialName("not_reviewed") var notReviewed: Int,
            @SerialName("unverified") var unverified: Int,
            @SerialName("spelling_grammar") var spellingGrammar: Int,
            @SerialName("inconsistent_placeholders") var inconsistentPlaceholders: Int,
            @SerialName("inconsistent_html") var inconsistentHtml: Int,
            @SerialName("different_number_of_urls") var differentNumberOfUrls: Int,
            @SerialName("different_urls") var differentUrls: Int,
            @SerialName("leading_whitespace") var leadingWhitespace: Int,
            @SerialName("trailing_whitespace") var trailingWhitespace: Int,
            @SerialName("different_number_of_email_address") var differentNumberOfEmailAddress: Int,
            @SerialName("different_email_address") var differentEmailAddress: Int,
            @SerialName("different_brackets") var differentBrackets: Int,
            @SerialName("different_numbers") var differentNumbers: Int,
            @SerialName("double_space") var doubleSpace: Int,
            @SerialName("special_placeholder") var specialPlaceholder: Int,
            @SerialName("unbalanced_brackets") var unbalancedBrackets: Int

        )

        @Serializable
        data class Languages(
            @SerialName("language_id") var languageId: Int,
            @SerialName("language_iso") var languageIso: String,
            @SerialName("progress") var progress: Int,
            @SerialName("words_to_do") var wordsToDo: Int
        )
    }
}