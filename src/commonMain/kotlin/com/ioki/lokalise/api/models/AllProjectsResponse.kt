package com.ioki.lokalise.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllProjectsResponse(@SerialName("projects") val projects: List<RetrieveProjectResponse>)
