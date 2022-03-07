package page.smirnov.wallester.core_network.domain

import android.net.Uri
import java.net.URL

interface BuildUrl {
    fun exec(baseUrl: String, parameters: Map<String, Any> = emptyMap()): URL
}

// Should be internal if we use DI
class BuildUrlImpl : BuildUrl {

    override fun exec(baseUrl: String, parameters: Map<String, Any>): URL {
        return Uri
            .parse(baseUrl)
            .buildUpon()
            .apply {
                parameters.forEach { (key, value) ->
                    appendQueryParameter(key, value.toString())
                }
            }
            .build()
            .toString()
            .let(::URL)
    }
}
