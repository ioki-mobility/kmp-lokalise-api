package com.ioki.lokalise.api

import com.ioki.lokalise.api.models.AllProjectsQueryParams
import com.ioki.lokalise.api.models.DownloadFilesRequestBody
import com.ioki.lokalise.api.models.Error
import com.ioki.lokalise.api.models.ErrorWrapper
import com.ioki.lokalise.api.models.FileDownload
import com.ioki.lokalise.api.models.FileDownloadAsync
import com.ioki.lokalise.api.models.FileUpload
import com.ioki.lokalise.api.models.Project
import com.ioki.lokalise.api.models.Projects
import com.ioki.lokalise.api.models.RetrievedProcess
import com.ioki.lokalise.api.models.UploadFilesRequestBody
import com.ioki.lokalise.api.models.toStringValues
import com.ioki.result.Result
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

/**
 * The Lokalise object represents the Lokalise API reference.
 * Check it out [here](https://developers.lokalise.com/reference/lokalise-rest-api)
 */
interface Lokalise :
    LokaliseProjects,
    LokaliseFiles,
    LokaliseQueuedProcesses

interface LokaliseProjects {

    /**
     * Retrieve a project by id.
     * [Go to API docs](https://developers.lokalise.com/reference/retrieve-a-project)
     */
    suspend fun retrieveProject(projectId: String): Result<Project, Error>

    /**
     * List all projects.
     * [Go to API docs](https://developers.lokalise.com/reference/list-all-projects)
     */
    suspend fun allProjects(params: AllProjectsQueryParams? = null): Result<Projects, Error>
}

interface LokaliseFiles {

    /**
     * Download files.
     * [Go to API docs](https://developers.lokalise.com/reference/download-files)
     */
    suspend fun downloadFiles(projectId: String, requestBody: DownloadFilesRequestBody): Result<FileDownload, Error>

    /**
     * Download files (Async).
     * [Go to API docs](https://developers.lokalise.com/reference/download-files-async)
     */
    suspend fun downloadFilesAsync(
        projectId: String,
        requestBody: DownloadFilesRequestBody,
    ): Result<FileDownloadAsync, Error>

    /**
     * Upload files.
     * [Go to API docs](https://developers.lokalise.com/reference/upload-a-file)
     */
    suspend fun uploadFile(projectId: String, requestBody: UploadFilesRequestBody): Result<FileUpload, Error>
}

interface LokaliseQueuedProcesses {

    /**
     * Retrieve a process.
     * [Go to API docs](https://developers.lokalise.com/reference/retrieve-a-process)
     */
    suspend fun retrieveProcess(projectId: String, processId: String): Result<RetrievedProcess, Error>
}

/**
 * Creates a new [Lokalise] object.
 */
fun Lokalise(token: String, fullLoggingEnabled: Boolean = false): Lokalise = Lokalise(token, fullLoggingEnabled, null)

/**
 * Creates a new [Lokalise] object.
 */
internal fun Lokalise(
    token: String,
    fullLoggingEnabled: Boolean = false,
    httpClientEngine: HttpClientEngine? = null,
): Lokalise {
    val clientConfig: HttpClientConfig<*>.() -> Unit = {
        install(ContentNegotiation) {
            val json = Json {
                classDiscriminator = "type"
                ignoreUnknownKeys = true
            }
            json(json)
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
        if (httpClientEngine != null) {
            HttpClient(httpClientEngine, clientConfig)
        } else {
            HttpClient(clientConfig)
        }

    return LokaliseClient(client)
}

private class LokaliseClient(private val httpClient: HttpClient) : Lokalise {

    override suspend fun retrieveProject(projectId: String): Result<Project, Error> = httpClient
        .get("projects/$projectId")
        .toResult()

    override suspend fun allProjects(params: AllProjectsQueryParams?): Result<Projects, Error> = httpClient
        .get("projects") {
            params?.toStringValues()?.let {
                url.parameters.appendAll(it)
            }
        }
        .toResult()

    override suspend fun downloadFiles(
        projectId: String,
        requestBody: DownloadFilesRequestBody,
    ): Result<FileDownload, Error> = httpClient
        .post("projects/$projectId/files/download") { setBody(requestBody) }
        .toResult()

    override suspend fun downloadFilesAsync(
        projectId: String,
        requestBody: DownloadFilesRequestBody,
    ): Result<FileDownloadAsync, Error> = httpClient
        .post("projects/$projectId/files/async-download") { setBody(requestBody) }
        .toResult()

    override suspend fun uploadFile(
        projectId: String,
        requestBody: UploadFilesRequestBody,
    ): Result<FileUpload, Error> = httpClient
        .post("projects/$projectId/files/upload") { setBody(requestBody) }
        .toResult()

    override suspend fun retrieveProcess(projectId: String, processId: String): Result<RetrievedProcess, Error> =
        httpClient
            .get("projects/$projectId/processes/$processId")
            .toResult()
}

private suspend inline fun <reified T> HttpResponse.toResult(): Result<T, Error> = if (status.value in 200..299) {
    Result.Success(body<T>())
} else {
    Result.Failure(body<ErrorWrapper>().error)
}
