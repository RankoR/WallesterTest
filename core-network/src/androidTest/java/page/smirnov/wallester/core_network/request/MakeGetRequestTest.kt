@file:OptIn(ExperimentalCoroutinesApi::class)

package page.smirnov.wallester.core_network.request

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import page.smirnov.wallester.core_network.data.exception.RequestFailureException
import page.smirnov.wallester.core_network.domain.BuildUrlImpl
import page.smirnov.wallester.core_network.domain.MakeGetRequest
import page.smirnov.wallester.core_network.domain.MakeGetRequestImpl

/**
 * Normally we should use a mock web server, but we can't use libraries, so we'll make real calls
 */
class MakeGetRequestTest {

    private val makeGetRequest: MakeGetRequest = MakeGetRequestImpl(BuildUrlImpl(), Dispatchers.IO)

    @Test
    fun makeNormalRequest() {
        runTest {
            val result = makeGetRequest.exec("https://api.punkapi.com/v2/beers")
            Assert.assertTrue(result.exceptionOrNull().toString(), result.isSuccess)

            val response = result.getOrNull()

            Assert.assertTrue(response.orEmpty().isNotEmpty())
        }
    }

    @Test
    fun make404Request() {
        runTest {
            val result = makeGetRequest.exec("https://api.punkapi.com/this-url-does-not-exist")
            Assert.assertTrue(result.isFailure)

            val exception = result.exceptionOrNull()

            Assert.assertNotNull(exception)
            Assert.assertTrue(exception is RequestFailureException)

            exception as RequestFailureException

            Assert.assertEquals(404, exception.code)
        }
    }
}
