package page.smirnov.wallester.core_network.data.parser.base

import org.json.JSONObject

interface JsonObjectParser<out T> {
    fun parse(jsonObject: JSONObject): Result<T>
}
