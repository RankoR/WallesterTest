package page.smirnov.wallester.core_network.json

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import page.smirnov.wallester.core_network.util.toJSONObjectSequence

class JSONArrayExtensionsTest {

    @Test
    fun normalToJSONObjectSequence() {
        val jsonObjects = listOf(
            JSONObject("{\"id\": 1}"),
            JSONObject("{\"id\": 2}"),
            JSONObject("{\"id\": 3}"),
            JSONObject("{\"id\": 4}"),
        )

        val jsonArray = JSONArray().apply {
            jsonObjects.forEach(::put)
        }

        val resultJsonObjects = jsonArray.toJSONObjectSequence().toList()

        Assert.assertEquals(jsonObjects, resultJsonObjects)
    }
}
