package page.smirnov.wallester.core_network

import org.junit.Assert
import org.junit.Test
import page.smirnov.wallester.core_network.domain.BuildUrl
import page.smirnov.wallester.core_network.domain.BuildUrlImpl
import java.net.MalformedURLException
import java.net.URL

class BuildUrlTest {

    private val buildUrl: BuildUrl = BuildUrlImpl()

    @Test
    fun buildWithoutParameters() {
        val url = buildUrl.exec("https://smirnov.page")

        Assert.assertEquals(URL("https://smirnov.page"), url)
    }

    @Test
    fun buildWithParameters() {
        val url = buildUrl.exec(
            "https://smirnov.page",
            mapOf(
                "param1" to "test",
                "param2" to 1337
            )
        )

        Assert.assertEquals(URL("https://smirnov.page?param1=test&param2=1337"), url)
    }

    @Test
    fun buildWithInvalidBaseUrl() {
        // Unfortunately, assertThrows is not available on Android
        try {
            buildUrl.exec("this is an invalid value")

            Assert.fail("Expected MalformedURLException")
        } catch (e: Throwable) {
            if (e !is MalformedURLException) {
                Assert.fail("Expected MalformedURLException but got other exception: $e")
            }
        }
    }
}
