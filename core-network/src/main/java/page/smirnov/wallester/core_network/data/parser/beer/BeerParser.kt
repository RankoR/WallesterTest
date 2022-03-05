package page.smirnov.wallester.core_network.data.parser.beer

import org.json.JSONObject
import page.smirnov.wallester.core_network.data.model.Beer
import page.smirnov.wallester.core_network.data.parser.base.JsonObjectParser

/**
 * Normally I'd use KotlinX serialization, but we can't use libs
 * Should be in a separate feature module like «:beers», but it's too complicated for a test project
 */
interface BeerParser : JsonObjectParser<Beer>

class BeerParserImpl : BeerParser {

    override fun parse(jsonObject: JSONObject): Result<Beer> {
        return runCatching {
            // All fields are required, so just failing on missing field
            Beer(
                id = jsonObject.getLong("id"),
                name = jsonObject.getString("name"),
                abv = jsonObject.getDouble("abv").toFloat(),
                ebc = jsonObject.getDouble("ebc").toFloat(),
                ibu = jsonObject.getDouble("ibu").toFloat()
            )
        }
    }
}
