package page.smirnov.wallester.core_network.data.parser.beer

import org.json.JSONArray
import page.smirnov.wallester.core_network.data.parser.base.ResponseParser
import page.smirnov.wallester.core_network.util.toJSONObjectSequence
import page.smirnov.wallester.core_persistence.data.model.Beer

/**
 * Should be in a separate feature module like «:beers», but it's too complicated for a test project
 */
interface BeerListParser : ResponseParser<List<Beer>>

class BeerListParserImpl(
    private val beerParser: BeerParser = BeerParserImpl() // Normally it'll be injected with DI
) : BeerListParser {

    override fun parse(rawData: String): Result<List<Beer>> {
        return JSONArray(rawData) // TODO: It can throw
            .toJSONObjectSequence()
            .map(beerParser::parse)
            .merge()
    }

    /**
     * Transforms [Sequence<Result<T>>] to [Result<List<T>>]
     */
    private fun <T> Sequence<Result<T>>.merge(): Result<List<T>> {
        // In this case we just ignore failed entries
        // Other option is to fail the full chain on failure

        return filter { it.isSuccess }
            .map { it.getOrThrow() }
            .toList() // Performance doesn't matter here (25 or fewer items), so just convert to list right there
            .let { Result.success(it) }
    }
}
