package page.smirnov.wallester.core_network.util

import org.json.JSONArray
import org.json.JSONObject

fun JSONArray.toJSONObjectSequence(): Sequence<JSONObject> {
    var currentIndex = 0

    return generateSequence {
        return@generateSequence if (currentIndex >= length()) {
            null
        } else {
            getJSONObject(currentIndex++)
        }
    }
}
