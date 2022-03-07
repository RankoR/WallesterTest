package page.smirnov.wallester.core_network.parser

import org.json.JSONException
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import page.smirnov.wallester.core_network.data.parser.beer.BeerParser
import page.smirnov.wallester.core_network.data.parser.beer.BeerParserImpl
import page.smirnov.wallester.core_persistence.data.model.Beer

class BeerParserTest {

    private val parser: BeerParser = BeerParserImpl()

    @Test
    fun parseNormalBeer() {
        val beerResult = """
            {
            "id": 1,
            "name": "Buzz",
            "tagline": "A Real Bitter Experience.",
            "first_brewed": "09/2007",
            "description": "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
            "image_url": "https://images.punkapi.com/v2/keg.png",
            "abv": 4.5,
            "ibu": 60,
            "target_fg": 1010,
            "target_og": 1044,
            "ebc": 20,
            "srm": 10,
            "ph": 4.4,
            "attenuation_level": 75
            }
        """.let(::parseJson)

        Assert.assertTrue(beerResult.isSuccess)

        val beer = beerResult.getOrThrow()

        Assert.assertEquals(1, beer.id)
        Assert.assertEquals("Buzz", beer.name)
        Assert.assertEquals(4.5f, beer.abv)
        Assert.assertEquals(20f, beer.ebc)
        Assert.assertEquals(60f, beer.ibu)
    }

    @Test
    fun parseBeerWithoutFields() {
        val beerResult = """
            {
            "name": "Buzz",
            "tagline": "A Real Bitter Experience.",
            "first_brewed": "09/2007",
            "description": "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
            "image_url": "https://images.punkapi.com/v2/keg.png",
            "abv": 4.5,
            "ibu": 60,
            "target_fg": 1010,
            "target_og": 1044,
            "ebc": 20,
            "srm": 10,
            "ph": 4.4,
            "attenuation_level": 75
            }
        """.let(::parseJson)

        Assert.assertTrue(beerResult.isFailure)
        Assert.assertTrue(beerResult.exceptionOrNull() is JSONException)
    }

    private fun parseJson(json: String): Result<Beer> {
        val jsonObject = JSONObject(json)
        return parser.parse(jsonObject)
    }
}
