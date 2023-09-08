package com.ioki.lokalise.api

import com.ioki.lokalise.api.stubs.allProjectsJson
import com.ioki.lokalise.api.stubs.fileDownloadJson
import com.ioki.lokalise.api.stubs.fileUploadJson
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import io.ktor.utils.io.core.String
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LokaliseClientTest {

    @Test
    fun `test list all projects without params`() = runLokaliseTest(allProjectsJson) { lokalise, mockEngine ->
        lokalise.allProjects()

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertTrue(requestData.headers.contains("Content-Type", "application/json"))
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Get
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects"
        )
    }

    @Test
    fun `test list all projects with params`() = runLokaliseTest(allProjectsJson) { lokalise, mockEngine ->
        val params = mapOf(
            "limit" to 1,
            "filter_names" to "first,second",
        )

        lokalise.allProjects(queryParams = params)

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertTrue(requestData.headers.contains("Content-Type", "application/json"))
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Get
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects?limit=1&filter_names=first,second"
        )
    }

    @Test
    fun `test download files without params`() = runLokaliseTest(fileDownloadJson) { lokalise, mockEngine ->
        lokalise.downloadFiles(
            projectId = "projectId",
            format = "someFormat"
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.body.contentType,
            expected = ContentType.Application.Json
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Post
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/files/download"
        )
        assertEquals(
            actual = String(requestData.body.toByteArray()),
            expected = """{"format":"someFormat"}""".trimIndent()
        )
    }

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
            bodyParams = params
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.body.contentType,
            expected = ContentType.Application.Json
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Post
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/files/download"
        )
        assertEquals(
            actual = String(requestData.body.toByteArray()),
            expected = """{"original_filenames":true,"filter_langs":["en","fr","de"],"directory_prefix":"prefix","format":"xml"}""".trimIndent()
        )
    }

    @Test
    fun `test upload file without params`() = runLokaliseTest(fileUploadJson) { lokalise, mockEngine ->
        lokalise.uploadFile(
            projectId = "projectId",
            data = "data",
            filename = "path/to/file.xml",
            langIso = "en"
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.body.contentType,
            expected = ContentType.Application.Json
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Post
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/files/upload"
        )
        assertEquals(
            actual = String(requestData.body.toByteArray()),
            expected = """{"data":"data","filename":"path/to/file.xml","lang_iso":"en"}""".trimIndent()
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
            bodyParams = params
        )

        val requestData = mockEngine.requestHistory.first()
        assertHeaders(requestData.headers)
        assertEquals(
            actual = requestData.body.contentType,
            expected = ContentType.Application.Json
        )
        assertEquals(
            actual = requestData.method,
            expected = HttpMethod.Post
        )
        assertEquals(
            actual = requestData.url.toString(),
            expected = "https://api.lokalise.com/api2/projects/projectId/files/upload"
        )
        assertEquals(
            actual = String(requestData.body.toByteArray()),
            expected = """
                {"convert_placeholders":true,"tags":["tag1","tag2"],"filter_task_id":42,"data":"data","filename":"path/to/file.xml","lang_iso":"en"}
                """.trimIndent()
        )
    }

    private fun assertHeaders(headers: Headers) = assertTrue {
        headers.contains("Accept", "application/json") &&
            headers.contains("X-Api-Token", "sec3tT0k3n")
    }

    private fun createLokalise(
        httpClientEngine: HttpClientEngine,
        token: String = "sec3tT0k3n",
    ): Lokalise = Lokalise(
        token = token,
        fullLoggingEnabled = true,
        httpClientEngine = httpClientEngine
    )

    private fun createMockEngine(content: String): MockEngine = MockEngine { _ ->
        respond(
            content = content,
            headers = headersOf("Content-Type", ContentType.Application.Json.toString())
        )
    }

    private fun runLokaliseTest(
        httpJsonResponse: String,
        block: suspend (Lokalise, MockEngine) -> Unit
    ) = runBlocking {
        val engine = createMockEngine(httpJsonResponse)
        val lokalise = createLokalise(engine)
        block(lokalise, engine)
    }
}
