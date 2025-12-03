package com.ioki.lokalise.api

import com.ioki.lokalise.api.models.AllProjectsRequest
import com.ioki.lokalise.api.models.AllProjectsResponse
import com.ioki.lokalise.api.models.DownloadFilesRequest
import com.ioki.lokalise.api.models.Error
import com.ioki.lokalise.api.models.RetrieveProjectResponse
import com.ioki.lokalise.api.models.UploadFileRequest
import com.ioki.lokalise.api.stubs.allProjectsJson
import com.ioki.lokalise.api.stubs.downloadFilesAsyncJson
import com.ioki.lokalise.api.stubs.downloadFilesJson
import com.ioki.lokalise.api.stubs.downloadFilesWithUnknownFieldJson
import com.ioki.lokalise.api.stubs.errorJson
import com.ioki.lokalise.api.stubs.retrieveProcessAsyncExportJson
import com.ioki.lokalise.api.stubs.retrieveProcessFileImportJson
import com.ioki.lokalise.api.stubs.retrieveProjectJson
import com.ioki.lokalise.api.stubs.uploadFileJson
import com.ioki.result.Result
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.util.flattenEntries
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class LokaliseClientTest {

    @Test
    fun `test result value with error on retrieveProject but doesn't matter where`() = runLokaliseTest(
        errorJson,
        HttpStatusCode.NotFound,
    ) { lokalise, mockEngine ->
        val result = lokalise.retrieveProject("projectId")

        result.shouldBeInstanceOf<Result.Failure<Error>> {
            it.error.code shouldBe 404
            it.error.message shouldBe "Not Found"
        }
    }

    @Test
    fun `test result value without error on retrieveProject`() =
        runLokaliseTest(retrieveProjectJson) { lokalise, mockEngine ->
            val result = lokalise.retrieveProject("projectId")

            result.shouldBeInstanceOf<Result.Success<RetrieveProjectResponse>> {
                with(it.data) {
                    projectId shouldBe "string"
                    createdAtTimestamp shouldBe 0
                    settings.branching shouldBe true
                }
            }
        }

    @Test
    fun `test retrieveProject api call`() = runLokaliseTest(retrieveProjectJson) { lokalise, mockEngine ->
        lokalise.retrieveProject(
            projectId = "awesomeProjectId",
        )

        with(mockEngine.requestHistory.first()) {
            assertHeadersAndContentType()
            method shouldBe HttpMethod.Get
            url.toString() shouldBe "https://api.lokalise.com/api2/projects/awesomeProjectId"
        }
    }

    @Test
    fun `test result value with error on allProjects but doesn't matter where`() = runLokaliseTest(
        errorJson,
        HttpStatusCode.NotFound,
    ) { lokalise, mockEngine ->
        val result = lokalise.allProjects()

        result.shouldBeInstanceOf<Result.Failure<Error>> {
            it.error.code shouldBe 404
            it.error.message shouldBe "Not Found"
        }
    }

    @Test
    fun `test result value without error on allProjects but doesn't matter where`() =
        runLokaliseTest(allProjectsJson) { lokalise, mockEngine ->
            val result = lokalise.allProjects()

            result.shouldBeInstanceOf<Result.Success<AllProjectsResponse>> {
                it.data.projects.size shouldBe 1
                with(it.data.projects.first()) {
                    projectId shouldBe "string"
                    createdAtTimestamp shouldBe 0
                    settings.branching shouldBe true
                }
            }
        }

    @Test
    fun `test list all projects without params`() = runLokaliseTest(allProjectsJson) { lokalise, mockEngine ->
        lokalise.allProjects()

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
        requestData.method shouldBe HttpMethod.Get
        requestData.url.toString() shouldBe "https://api.lokalise.com/api2/projects"
    }

    @Suppress("ktlint:standard:max-line-length")
    @Test
    fun `test list all projects with params`() = runLokaliseTest(allProjectsJson) { lokalise, mockEngine ->
        val params = AllProjectsRequest(
            limit = 2,
            filterNames = "first,second",
            includeSettings = AllProjectsRequest.IncludeOption.INCLUDE,
        )

        lokalise.allProjects(params)

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
        requestData.method shouldBe HttpMethod.Get
        requestData.url.toString() shouldBe
            "https://api.lokalise.com/api2/projects?filter_names=first%2Csecond&include_settings=1&limit=2"
    }

    @Test
    fun `test download files without params`() = runLokaliseTest(downloadFilesJson) { lokalise, mockEngine ->
        val requestBody = DownloadFilesRequest(
            format = "someFormat",
        )

        lokalise.downloadFiles(
            projectId = "projectId",
            requestBody = requestBody,
        )

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
        requestData.method shouldBe HttpMethod.Post
        requestData.url.toString() shouldBe "https://api.lokalise.com/api2/projects/projectId/files/download"
        requestData.body.toByteArray().decodeToString() shouldBe """{"format":"someFormat"}""".trimIndent()
    }

    @Suppress("ktlint:standard:max-line-length")
    @Test
    fun `test download files with params`() = runLokaliseTest(downloadFilesJson) { lokalise, mockEngine ->
        val requestBody = DownloadFilesRequest(
            format = "xml",
            originalFilenames = true,
            filterLangs = listOf("en", "fr", "de"),
            directoryPrefix = "prefix",
        )

        lokalise.downloadFiles(
            projectId = "projectId",
            requestBody = requestBody,
        )

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
        requestData.method shouldBe HttpMethod.Post
        requestData.url.toString() shouldBe "https://api.lokalise.com/api2/projects/projectId/files/download"
        requestData.body.toByteArray().decodeToString() shouldBe
            """{"format":"xml","original_filenames":true,"directory_prefix":"prefix","filter_langs":["en","fr","de"]}""".trimIndent()
    }

    @Test
    fun `test download files async without params`() = runLokaliseTest(downloadFilesAsyncJson) { lokalise, mockEngine ->
        val requestBody = DownloadFilesRequest(
            format = "someFormat",
        )

        lokalise.downloadFilesAsync(
            projectId = "projectId",
            requestBody = requestBody,
        )

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
        requestData.method shouldBe HttpMethod.Post
        requestData.url.toString() shouldBe "https://api.lokalise.com/api2/projects/projectId/files/async-download"
        requestData.body.toByteArray().decodeToString() shouldBe """{"format":"someFormat"}""".trimIndent()
    }

    @Suppress("ktlint:standard:max-line-length")
    @Test
    fun `test download files async with params`() = runLokaliseTest(downloadFilesAsyncJson) { lokalise, mockEngine ->
        val requestBody = DownloadFilesRequest(
            format = "xml",
            originalFilenames = true,
            filterLangs = listOf("en", "fr", "de"),
            directoryPrefix = "prefix",
        )

        lokalise.downloadFilesAsync(
            projectId = "projectId",
            requestBody = requestBody,
        )

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
        requestData.method shouldBe HttpMethod.Post
        requestData.url.toString() shouldBe "https://api.lokalise.com/api2/projects/projectId/files/async-download"
        requestData.body.toByteArray().decodeToString() shouldBe
            """{"format":"xml","original_filenames":true,"directory_prefix":"prefix","filter_langs":["en","fr","de"]}""".trimIndent()
    }

    @Test
    fun `test upload file without params`() = runLokaliseTest(uploadFileJson) { lokalise, mockEngine ->
        val requestBody = UploadFileRequest(
            data = "data",
            filename = "path/to/file.xml",
            langIso = "en",
        )

        lokalise.uploadFile(
            projectId = "projectId",
            requestBody = requestBody,
        )

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
        requestData.method shouldBe HttpMethod.Post
        requestData.url.toString() shouldBe "https://api.lokalise.com/api2/projects/projectId/files/upload"
        requestData.body.toByteArray()
            .decodeToString() shouldBe """{"data":"data","filename":"path/to/file.xml","lang_iso":"en"}""".trimIndent()
    }

    @Test
    fun `test upload file with params`() = runLokaliseTest(uploadFileJson) { lokalise, mockEngine ->
        val requestBody = UploadFileRequest(
            data = "data",
            filename = "path/to/file.xml",
            langIso = "en",
            convertPlaceholders = true,
            tags = listOf("tag1", "tag2"),
            filterTaskId = 42,
        )

        lokalise.uploadFile(
            projectId = "projectId",
            requestBody = requestBody,
        )

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
        requestData.method shouldBe HttpMethod.Post
        requestData.url.toString() shouldBe "https://api.lokalise.com/api2/projects/projectId/files/upload"
        requestData.body.toByteArray().decodeToString() shouldBe """
            {"data":"data","filename":"path/to/file.xml","lang_iso":"en","convert_placeholders":true,"tags":["tag1","tag2"],"filter_task_id":42}
        """.trimIndent()
    }

    @Test
    fun `test retrieve process file import`() = runLokaliseTest(retrieveProcessFileImportJson) { lokalise, mockEngine ->
        lokalise.retrieveProcess(
            projectId = "projectId",
            processId = "processId",
        )

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
        requestData.method shouldBe HttpMethod.Get
        requestData.url.toString() shouldBe "https://api.lokalise.com/api2/projects/projectId/processes/processId"
    }

    @Test
    fun `test retrieve process async export`() = runLokaliseTest(retrieveProcessAsyncExportJson) {
            lokalise,
            mockEngine,
        ->
        lokalise.retrieveProcess(
            projectId = "projectId",
            processId = "processId",
        )

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
        requestData.method shouldBe HttpMethod.Get
        requestData.url.toString() shouldBe "https://api.lokalise.com/api2/projects/projectId/processes/processId"
    }

    @Test
    fun `test ignore unknown fields`() = runLokaliseTest(downloadFilesWithUnknownFieldJson) { lokalise, mockEngine ->
        val requestBody = DownloadFilesRequest(
            format = "someFormat",
        )

        lokalise.downloadFiles(
            projectId = "projectId",
            requestBody = requestBody,
        )

        val requestData = mockEngine.requestHistory.first()
        requestData.assertHeadersAndContentType()
    }

    private fun HttpRequestData.assertHeadersAndContentType() {
        headers.flattenEntries().sortedBy { it.first }.shouldContainAll(
            "Accept" to "application/json",
            "X-Api-Token" to "sec3tT0k3n",
        )
        if (method == HttpMethod.Post) {
            body.contentType shouldBe ContentType.Application.Json
        } else {
            body.contentType shouldBe null
        }
    }

    private fun runLokaliseTest(
        httpJsonResponse: String,
        httpStatusCode: HttpStatusCode = HttpStatusCode.OK,
        block: suspend (Lokalise, MockEngine) -> Unit,
    ) = runBlocking {
        val engine = createMockEngine(httpJsonResponse, httpStatusCode)
        val lokalise = createLokalise(engine)
        block(lokalise, engine)
    }
}

private fun createMockEngine(content: String, statusCode: HttpStatusCode): MockEngine = MockEngine { _ ->
    respond(
        content = content,
        headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
        status = statusCode,
    )
}

private fun createLokalise(httpClientEngine: HttpClientEngine, token: String = "sec3tT0k3n"): Lokalise = Lokalise(
    token = token,
    fullLoggingEnabled = true,
    httpClientEngine = httpClientEngine,
)
