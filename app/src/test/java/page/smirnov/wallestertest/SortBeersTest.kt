package page.smirnov.wallestertest

import org.junit.Assert
import org.junit.Test
import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallestertest.data.model.SortingMode
import page.smirnov.wallestertest.domain.interactor.SortBeers
import page.smirnov.wallestertest.domain.interactor.SortBeersImpl

class SortBeersTest {

    private val sortBeers: SortBeers = SortBeersImpl()

    @Test
    fun `sort by name`() {
        val beers = listOf(
            Beer(id = 1, name = "a", abv = 3.5f, ebc = 4f, ibu = 15f),
            Beer(id = 2, name = "b", abv = 1.5f, ebc = 2f, ibu = 215f),
            Beer(id = 3, name = "c", abv = 4.5f, ebc = 3f, ibu = 153f),
            Beer(id = 4, name = "d", abv = 6.5f, ebc = 1f, ibu = 155f),
            Beer(id = 5, name = "e", abv = 7.5f, ebc = 1000f, ibu = 151f),
        ).shuffled()

        val sorted = sortBeers.exec(beers, SortingMode.NAME)

        Assert.assertEquals(1, sorted[0].id)
        Assert.assertEquals(2, sorted[1].id)
        Assert.assertEquals(3, sorted[2].id)
        Assert.assertEquals(4, sorted[3].id)
        Assert.assertEquals(5, sorted[4].id)
    }

    @Test
    fun `sort by abv`() {
        val beers = listOf(
            Beer(id = 1, name = "aev", abv = 1.5f, ebc = 4f, ibu = 15f),
            Beer(id = 2, name = "g3gb", abv = 2.9f, ebc = 2f, ibu = 215f),
            Beer(id = 3, name = "1ffc", abv = 40.5f, ebc = 3f, ibu = 153f),
            Beer(id = 4, name = "addwd", abv = 63.5f, ebc = 1f, ibu = 155f),
            Beer(id = 5, name = "qf2e", abv = 89.5f, ebc = 1000f, ibu = 151f),
        ).shuffled()

        val sorted = sortBeers.exec(beers, SortingMode.ABV)

        Assert.assertEquals(1, sorted[0].id)
        Assert.assertEquals(2, sorted[1].id)
        Assert.assertEquals(3, sorted[2].id)
        Assert.assertEquals(4, sorted[3].id)
        Assert.assertEquals(5, sorted[4].id)
    }

    @Test
    fun `sort by ebc`() {
        val beers = listOf(
            Beer(id = 1, name = "aev", abv = 34.5f, ebc = 1f, ibu = 15f),
            Beer(id = 2, name = "g3gb", abv = 2.9f, ebc = 2f, ibu = 215f),
            Beer(id = 3, name = "1ffc", abv = 0.5f, ebc = 54f, ibu = 153f),
            Beer(id = 4, name = "addwd", abv = 63.5f, ebc = 55f, ibu = 155f),
            Beer(id = 5, name = "qf2e", abv = 89.5f, ebc = 68f, ibu = 151f),
        ).shuffled()

        val sorted = sortBeers.exec(beers, SortingMode.EBC)

        Assert.assertEquals(1, sorted[0].id)
        Assert.assertEquals(2, sorted[1].id)
        Assert.assertEquals(3, sorted[2].id)
        Assert.assertEquals(4, sorted[3].id)
        Assert.assertEquals(5, sorted[4].id)
    }

    @Test
    fun `sort by ibu`() {
        val beers = listOf(
            Beer(id = 1, name = "aev", abv = 34.5f, ebc = 31f, ibu = 15f),
            Beer(id = 2, name = "g3gb", abv = 2.9f, ebc = 32f, ibu = 15.2f),
            Beer(id = 3, name = "1ffc", abv = 0.5f, ebc = 5f, ibu = 33.7f),
            Beer(id = 4, name = "addwd", abv = 63.5f, ebc = 5.1f, ibu = 150f),
            Beer(id = 5, name = "qf2e", abv = 89.5f, ebc = 1.68f, ibu = 151f),
        ).shuffled()

        val sorted = sortBeers.exec(beers, SortingMode.IBU)

        Assert.assertEquals(1, sorted[0].id)
        Assert.assertEquals(2, sorted[1].id)
        Assert.assertEquals(3, sorted[2].id)
        Assert.assertEquals(4, sorted[3].id)
        Assert.assertEquals(5, sorted[4].id)
    }
}
