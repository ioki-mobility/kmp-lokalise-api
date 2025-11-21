package com.ioki.lokalise.api.models

import io.ktor.util.StringValues

data class AllProjectsRequest(
    val filterTeamId: Int? = null,
    val filterNames: String? = null,
    val includeStatistics: IncludeOption? = null,
    val includeSettings: IncludeOption? = null,
    val limit: Int? = null,
    val page: Int? = null,
) {
    enum class IncludeOption(val value: Int) {
        EXCLUDE(0),
        INCLUDE(1),
    }
}

internal fun AllProjectsRequest.toStringValues(): StringValues = StringValues.build {
    filterTeamId?.let { append("filter_team_id", it.toString()) }
    filterNames?.let { append("filter_names", it) }
    includeStatistics?.let { append("include_statistics", it.value.toString()) }
    includeSettings?.let { append("include_settings", it.value.toString()) }
    limit?.let { append("limit", it.toString()) }
    page?.let { append("page", it.toString()) }
}
