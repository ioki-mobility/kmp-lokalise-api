package com.ioki.lokalise.api

import com.ioki.lokalise.api.models.ErrorWrapper
import com.ioki.lokalise.api.models.FileDownload
import com.ioki.lokalise.api.models.FileUpload
import com.ioki.lokalise.api.models.Projects
import com.ioki.lokalise.api.models.RetrievedProcess
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * The Lokalise object represents the Lokalise API reference.
 * Check it out [here](https://developers.lokalise.com/reference/lokalise-rest-api)
 */
interface Lokalise : LokaliseProjects, LokaliseFiles, LokaliseQueuedProcesses

interface LokaliseProjects {

    /**
     * List all projects.
     * [Go to API docs](https://developers.lokalise.com/reference/list-all-projects)
     */
    suspend fun allProjects(
        queryParams: Map<String, Any> = emptyMap()
    ): Result<Projects>
}

interface LokaliseFiles {

    /**
     * Download files.
     * [Go to API docs](https://developers.lokalise.com/reference/download-files)
     */
    suspend fun downloadFiles(
        projectId: String,
        format: String,
        bodyParams: Map<String, Any> = emptyMap()
    ): Result<FileDownload>

    /**
     * Upload files.
     * [Go to API docs](https://developers.lokalise.com/reference/upload-a-file)
     */
    suspend fun uploadFile(
        projectId: String,
        data: String,
        filename: String,
        langIso: String,
        bodyParams: Map<String, Any> = emptyMap()
    ): Result<FileUpload>
}

interface LokaliseQueuedProcesses {

    /**
     * Retrieve a process.
     * [Go to API docs](https://developers.lokalise.com/reference/retrieve-a-process)
     */
    suspend fun retrieveProcess(
        projectId: String,
        processId: String,
    ): Result<RetrievedProcess>
}

/**
 * Creates a new [Lokalise] object.
 */
fun Lokalise(
    token: String,
    fullLoggingEnabled: Boolean = false,
): Lokalise = Lokalise(token, fullLoggingEnabled, null)

/**
 * Creates a new [Lokalise] object.
 */
internal fun Lokalise(
    token: String,
    fullLoggingEnabled: Boolean = false,
    httpClientEngine: HttpClientEngine? = null
): Lokalise {
    val clientConfig: HttpClientConfig<*>.() -> Unit = {
        install(ContentNegotiation) {
            json(Json{ ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = if (fullLoggingEnabled) LogLevel.ALL else LogLevel.HEADERS
        }

        defaultRequest {
            url("https://api.lokalise.com/api2/")
            contentType(ContentType.Application.Json)
            header("X-Api-Token", token)
        }
    }

    val client =
        if (httpClientEngine != null) HttpClient(httpClientEngine, clientConfig)
        else HttpClient(clientConfig)

    return LokaliseClient(client)
}

private class LokaliseClient(
    private val httpClient: HttpClient,
) : Lokalise {

    override suspend fun allProjects(queryParams: Map<String, Any>): Result<Projects> {
        val requestParams = queryParams
            .map { "${it.key}=${it.value}" }
            .joinToString(separator = "&")
            .run { if (isNotBlank()) "?$this" else this }

        return httpClient
            .get("projects$requestParams")
            .toResult()
    }

    override suspend fun downloadFiles(
        projectId: String,
        format: String,
        bodyParams: Map<String, Any>
    ): Result<FileDownload> {
        val requestBody = bodyParams.toMutableMap()
            .apply { put("format", format) }
            .toRequestBody()

        return httpClient
            .post("projects/$projectId/files/download") { setBody(requestBody) }
            .toResult()
    }

    override suspend fun uploadFile(
        projectId: String,
        data: String,
        filename: String,
        langIso: String,
        bodyParams: Map<String, Any>
    ): Result<FileUpload> {
        val requestBody = bodyParams.toMutableMap()
            .apply {
                put("data", data)
                put("filename", filename)
                put("lang_iso", langIso)
            }
            .toRequestBody()

        return httpClient
            .post("projects/$projectId/files/upload") { setBody(requestBody) }
            .toResult()
    }

    override suspend fun retrieveProcess(
        projectId: String,
        processId: String
    ): Result<RetrievedProcess> {
        return httpClient
            .get("projects/$projectId/processes/$processId")
            .toResult()
    }
}

private suspend inline fun <reified T> HttpResponse.toResult(): Result<T> =
    if (status.value in 200..299) Result.Success(body<T>())
    else Result.Failure(body<ErrorWrapper>().error)

/**
 * Found at
 * [kotlinx.serialization/issues#746](https://github.com/Kotlin/kotlinx.serialization/issues/746#issuecomment-863099397)
 */
private fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull
    is JsonElement -> this
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    is Array<*> -> JsonArray(map { it.toJsonElement() })
    is List<*> -> JsonArray(map { it.toJsonElement() })
    is Map<*, *> -> JsonObject(map { it.key.toString() to it.value.toJsonElement() }.toMap())
    else -> error("Unknown type!")
}

/**
 * Returns a Map with values `Any` to a Map with values of `JsonElement`
 */
private fun Map<String, Any>.toRequestBody() = mapValues { it.value.toJsonElement() }