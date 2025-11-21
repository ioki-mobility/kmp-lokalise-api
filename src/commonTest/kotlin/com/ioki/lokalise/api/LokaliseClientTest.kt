package com.ioki.lokalise.api

import com.ioki.lokalise.api.models.AllProjectsQueryParams
import com.ioki.lokalise.api.stubs.errorJson
import com.ioki.lokalise.api.stubs.fileDownloadAsyncJson
import com.ioki.lokalise.api.stubs.fileDownloadJson
import com.ioki.lokalise.api.stubs.fileDownloadWithUnknownFieldJson
import com.ioki.lokalise.api.stubs.fileUploadJson
import com.ioki.lokalise.api.stubs.projectJson
import com.ioki.lokalise.api.stubs.projectsJson
import com.ioki.lokalise.api.stubs.retrieveProcessAsyncExportJson
import com.ioki.lokalise.api.stubs.retrieveProcessFileImportJson
import com.ioki.result.Result
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LokaliseClientTest {

    @Test
    fun `test result value with error on retrieveProject but doesn't matter where`() = runLokaliseTest(
        errorJson,
        HttpStatusCode.NotFound,
    ) { lokalise, mockEngine ->
        val result = lokalise.retrieveProject("projectId")

        assertTrue(result is Result.Failure)
        assertTrue(result.error.code == 404)
        assertTrue(result.error.message == "Not Found")
    }

    @Test
    fun `test result value without error on retrieveProject`() = runLokaliseTest(projectJson) { lokalise, mockEngine ->
        val result = lokalise.retrieveProject("projectId")

        assertTrue(result is Result.Success)
        with(result.data) {
            assertEquals(
                expected = projectId,
                actual = "string",
            )
            assertEquals(
                expected = createdAtTimestamp,
                actual = 0,
            )
            assertEquals(
                expected = settings.branching,
                actual = true,
            )
        }
    }

    @Test
    fun `test retrieveProject api call`() = runLokaliseTest(projectJson) { lokalise, mockEngine ->
        lokalise.retrieveProject(
            projectId = "awesomeProjectId",
        )

        val requestData = mockEngine.requestHistory.first()
        assertEquals(
            actual = requestData.body.contentType,
            expected = null,
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Get,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/awesomeProjectId",
        )
    }

    @Test
    fun `test result value with error on allProjects but doesn't matter where`() = runLokaliseTest(
        errorJson,
        HttpStatusCode.NotFound,
    ) { lokalise, mockEngine ->
        val result = lokalise.allProjects()

        assertTrue(result is Result.Failure)
        assertTrue(result.error.code == 404)
        assertTrue(result.error.message == "Not Found")
    }

    @Test
    fun `test result value without error on allProjects but doesn't matter where`() =
        runLokaliseTest(projectsJson) { lokalise, mockEngine ->
            val result = lokalise.allProjects()

            assertTrue(result is Result.Success)
            assertTrue(result.data.projects.size == 1)
            with(result.data.projects.first()) {
                assertEquals(
                    expected = projectId,
                    actual = "string",
                )
                assertEquals(
                    expected = createdAtTimestamp,
                    actual = 0,
                )
                assertEquals(
                    expected = settings.branching,
                    actual = true,
                )
            }
        }

    @Test
    fun `test list all projects without params`() = runLokaliseTest(projectsJson) { lokalise, mockEngine ->
        lokalise.allProjects()

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertTrue(requestData.headers.contains("Content-Type", "application/json"))
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Get,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects",
        )
    }

    @Test
    fun `test list all projects with params`() = runLokaliseTest(projectsJson) { lokalise, mockEngine ->
        val params = AllProjectsQueryParams(
            limit = 2,
            filterNames = "first,second",
            includeSettings = AllProjectsQueryParams.IncludeOption.INCLUDE
        )

        lokalise.allProjects(params)

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertTrue(requestData.headers.contains("Content-Type", "application/json"))
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Get,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects?filter_names=first%2Csecond&include_settings=1&limit=2",
        )
    }

    @Test
    fun `test download files without params`() = runLokaliseTest(fileDownloadJson) { lokalise, mockEngine ->
        lokalise.downloadFiles(
            projectId = "projectId",
            format = "someFormat",
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.body.contentType,
            expected = ContentType.Application.Json,
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Post,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/files/download",
        )
        assertEquals(
            actual = requestData.body.toByteArray().decodeToString(),
            expected = """{"format":"someFormat"}""".trimIndent(),
        )
    }

    @Suppress("ktlint:standard:max-line-length")
    @Test
    fun `test download files with params`() = runLokaliseTest(fileDownloadJson) { lokalise, mockEngine ->
        val params = mapOf(
            "original_filenames" to true,
            "filter_langs" to listOf("en", "fr", "de"),
            "directory_prefix" to "prefix",
        )

        lokalise.downloadFiles(
            projectId = "projectId",
            format = "xml",
            bodyParams = params,
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.body.contentType,
            expected = ContentType.Application.Json,
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Post,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/files/download",
        )
        assertEquals(
            actual = requestData.body.toByteArray().decodeToString(),
            expected = """{"original_filenames":true,"filter_langs":["en","fr","de"],"directory_prefix":"prefix","format":"xml"}""".trimIndent(),
        )
    }

    @Test
    fun `test download files async without params`() = runLokaliseTest(fileDownloadAsyncJson) { lokalise, mockEngine ->
        lokalise.downloadFilesAsync(
            projectId = "projectId",
            format = "someFormat",
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.body.contentType,
            expected = ContentType.Application.Json,
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Post,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/files/async-download",
        )
        assertEquals(
            actual = requestData.body.toByteArray().decodeToString(),
            expected = """{"format":"someFormat"}""".trimIndent(),
        )
    }

    @Suppress("ktlint:standard:max-line-length")
    @Test
    fun `test download files async with params`() = runLokaliseTest(fileDownloadAsyncJson) { lokalise, mockEngine ->
        val params = mapOf(
            "original_filenames" to true,
            "filter_langs" to listOf("en", "fr", "de"),
            "directory_prefix" to "prefix",
        )

        lokalise.downloadFilesAsync(
            projectId = "projectId",
            format = "xml",
            bodyParams = params,
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.body.contentType,
            expected = ContentType.Application.Json,
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Post,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/files/async-download",
        )
        assertEquals(
            actual = requestData.body.toByteArray().decodeToString(),
            expected = """{"original_filenames":true,"filter_langs":["en","fr","de"],"directory_prefix":"prefix","format":"xml"}""".trimIndent(),
        )
    }

    @Test
    fun `test upload file without params`() = runLokaliseTest(fileUploadJson) { lokalise, mockEngine ->
        lokalise.uploadFile(
            projectId = "projectId",
            data = "data",
            filename = "path/to/file.xml",
            langIso = "en",
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.body.contentType,
            expected = ContentType.Application.Json,
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Post,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/files/upload",
        )
        assertEquals(
            actual = requestData.body.toByteArray().decodeToString(),
            expected = """{"data":"data","filename":"path/to/file.xml","lang_iso":"en"}""".trimIndent(),
        )
    }

    @Test
    fun `test upload file with params`() = runLokaliseTest(fileUploadJson) { lokalise, mockEngine ->
        val params = mapOf(
            "convert_placeholders" to true,
            "tags" to listOf("tag1", "tag2"),
            "filter_task_id" to 42,
        )

        lokalise.uploadFile(
            projectId = "projectId",
            data = "data",
            filename = "path/to/file.xml",
            langIso = "en",
            bodyParams = params,
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.body.contentType,
            expected = ContentType.Application.Json,
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Post,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/files/upload",
        )
        assertEquals(
            actual = requestData.body.toByteArray().decodeToString(),
            expected = """
                {"convert_placeholders":true,"tags":["tag1","tag2"],"filter_task_id":42,"data":"data","filename":"path/to/file.xml","lang_iso":"en"}
            """.trimIndent(),
        )
    }

    @Test
    fun `test retrieve process file import`() = runLokaliseTest(retrieveProcessFileImportJson) { lokalise, mockEngine ->
        lokalise.retrieveProcess(
            projectId = "projectId",
            processId = "processId",
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Get,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/processes/processId",
        )
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
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Get,
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/processes/processId",
        )
    }

    @Test
    fun `test ignore unknown fields`() = runLokaliseTest(fileDownloadWithUnknownFieldJson) { lokalise, mockEngine ->
        lokalise.downloadFiles(
            projectId = "projectId",
            format = "someFormat",
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
    }

    private fun assertHeaders(headers: Headers) = assertTrue {
        headers.contains("Accept", "application/json") &&
            headers.contains("X-Api-Token", "sec3tT0k3n")
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
