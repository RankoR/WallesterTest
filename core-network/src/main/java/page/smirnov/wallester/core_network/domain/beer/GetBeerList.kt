package page.smirnov.wallester.core_network.domain.beer

import page.smirnov.wallester.core.util.extension.flatMap
import page.smirnov.wallester.core_network.data.parser.beer.BeerListParser
import page.smirnov.wallester.core_network.domain.MakeGetRequest
import page.smirnov.wallester.core_persistence.data.model.Beer

interface GetBeerList {
    suspend fun exec(page: Int): Result<List<Beer>>
}

/**
 * Should be in a separate feature module like «:beers», but it's too complicated for a test project
 */
class GetBeerListImpl(
    private val makeGetRequest: MakeGetRequest, // Normally it'll be injected with DI
    private val beerListParser: BeerListParser // Normally it'll be injected with DI
) : GetBeerList {

    override suspend fun exec(page: Int): Result<List<Beer>> {
        return makeGetRequest
            .exec(
                baseUrl = BASE_URL,
                parameters = mapOf(
                    "page" to page
                )
            )
            .flatMap(beerListParser::parse)
    }

    private companion object {
        private const val BASE_URL = "https://api.punkapi.com/v2/beers"
    }
}
