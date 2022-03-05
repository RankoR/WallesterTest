package page.smirnov.wallester.core_network.parser

import org.junit.Assert
import org.junit.Test
import page.smirnov.wallester.core_network.data.model.Beer
import page.smirnov.wallester.core_network.data.parser.beer.BeerListParser
import page.smirnov.wallester.core_network.data.parser.beer.BeerListParserImpl
import page.smirnov.wallester.core_network.data.parser.beer.BeerParserImpl

class BeerListParserTest {

    private val parser: BeerListParser = BeerListParserImpl(
        beerParser = BeerParserImpl() // Should be mocked, but no time for it
    )

    @Test
    fun parseNormalBeerList() {
        val json = """
           [{
                "id": 1,
                "name": "Buzz",
                "abv": 4.5,
                "ibu": 60,
                "ebc": 20
           },
           {
                "id": 2,
                "name": "Buzz2",
                "abv": 4.5,
                "ibu": 60,
                "ebc": 20
           },
           {
                "id": 3,
                "name": "Buzz3",
                "abv": 4.5,
                "ibu": 60,
                "ebc": 20
           }]
        """

        val result = parser.parse(json)

        Assert.assertTrue(result.isSuccess)

        val beers = result.getOrThrow()

        Assert.assertEquals(3, beers.size)
    }

    @Test
    fun parseEmptyBeerList() {
        val json = "[]"

        val result = parser.parse(json)

        Assert.assertTrue(result.isSuccess)

        val beers = result.getOrThrow()

        Assert.assertEquals(0, beers.size)
    }

    @Test
    fun parseBeerListWithOneBroken() {
        val json = """
           [{
                "id": 1,
                "name": "Buzz",
                "abv": 4.5,
                "ibu": 60,
                "ebc": 20
           },
           {
                "name": "Buzz2",
                "abv": 4.5,
                "ibu": 60,
                "ebc": 20
           },
           {
                "id": 3,
                "name": "Buzz3",
                "abv": 4.5,
                "ibu": 60,
                "ebc": 20
           }]
        """

        val result = parser.parse(json)

        Assert.assertTrue(result.isSuccess)

        val beers = result.getOrThrow()

        Assert.assertEquals(2, beers.size)
    }

    private fun parseJson(json: String): Result<List<Beer>> {
        return parser.parse(json)
    }
}
