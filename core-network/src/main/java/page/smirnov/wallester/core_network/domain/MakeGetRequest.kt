package page.smirnov.wallester.core_network.domain

import kotlinx.coroutines.CoroutineDispatcher
import page.smirnov.wallester.core.util.withContextCatching
import page.smirnov.wallester.core_network.data.exception.RequestFailureException
import java.io.InputStream
import java.net.HttpURLConnection

interface MakeGetRequest {
    // Normally it should return something more generic, like byte array
    // But in our case we work only with text (JSON), so it's fine
    suspend fun exec(
        baseUrl: String,
        parameters: Map<String, Any> = emptyMap()
    ): Result<String>
}

// Should be internal if we use DI
class MakeGetRequestImpl(
    private val buildUrl: BuildUrl,
    private val coroutineDispatcher: CoroutineDispatcher
) : MakeGetRequest {

    override suspend fun exec(baseUrl: String, parameters: Map<String, Any>): Result<String> {
        return withContextCatching(coroutineDispatcher) {
            // Using HttpURLConnection as we can't use any libs
            // There is no no-blocking HttpURLConnection implementation, so just suppressing
            @Suppress("BlockingMethodInNonBlockingContext")
            buildUrl
                .exec(baseUrl, parameters)
                .openConnection()
                .let { it as HttpURLConnection }
                .apply {
                    requestMethod = METHOD_GET

                    // Hardcoding content type as we'll use only JSON
                    setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
                    setRequestProperty(HEADER_ACCEPT, CONTENT_TYPE_JSON)
                }
                .also { connection ->
                    if (connection.responseCode != CODE_OK) {
                        throw RequestFailureException(connection.responseCode, connection.errorStream.readText())
                    }
                }
                .inputStream
                .readText()
        }
    }

    private fun InputStream.readText(): String {
        return use { inputStream ->
            inputStream
                .bufferedReader()
                .readText()
        }
    }

    private companion object {
        private const val METHOD_GET = "GET"

        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val HEADER_ACCEPT = "Accept"

        private const val CONTENT_TYPE_JSON = "application/json"

        private const val CODE_OK = 200
    }
}
