@file:OptIn(ExperimentalCoroutinesApi::class)

package page.smirnov.wallester.core

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import page.smirnov.wallester.core_persistence.data.db.FavoritesDbHelper
import page.smirnov.wallester.core_persistence.data.model.Beer
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepository
import page.smirnov.wallester.core_persistence.data.repository.FavoritesRepositoryImpl
import kotlin.random.Random

class FavoritesRepositoryTest {

    private val repository: FavoritesRepository = FavoritesRepositoryImpl(
        favoritesDbHelper = FavoritesDbHelper(InstrumentationRegistry.getInstrumentation().targetContext),
        dispatcher = Dispatchers.IO
    )

    @Before
    fun deleteDb() {
        InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase(FavoritesDbHelper.DB_NAME)
    }

    @Test
    fun addFavoriteBeer() {
        runTest {
            val favoriteBeers = (0..99)
                .map {
                    Beer(
                        id = it.toLong(),
                        name = "Beer #$it",
                        abv = Random.nextFloat(),
                        ebc = Random.nextFloat(),
                        ibu = Random.nextFloat(),
                        isFavorite = true
                    )
                }
                .toList()

            favoriteBeers.forEach {
                val addResult = repository.addFavoriteBeer(it)
                Assert.assertTrue(addResult.isSuccess)
            }

            val getResult = repository.getFavoriteBeers()
            Assert.assertTrue(getResult.isSuccess)

            val gotFavoriteBeers = getResult.getOrThrow()

            favoriteBeers.forEachIndexed { index, favoriteBeer -> // To check items order
                Assert.assertEquals(favoriteBeer, gotFavoriteBeers[index])
            }
        }
    }

    @Test
    fun removeFavoriteBeer() {
        runTest {
            val favoriteBeers = (0..99)
                .map {
                    Beer(
                        id = it.toLong(),
                        name = "Beer #$it",
                        abv = Random.nextFloat(),
                        ebc = Random.nextFloat(),
                        ibu = Random.nextFloat(),
                        isFavorite = true
                    )
                }
                .toList()

            favoriteBeers.forEach {
                val addResult = repository.addFavoriteBeer(it)
                Assert.assertTrue(addResult.isSuccess)
            }

            val getResult = repository.getFavoriteBeers()
            Assert.assertTrue(getResult.isSuccess)

            Assert.assertEquals(favoriteBeers.size, getResult.getOrThrow().size)

            val favoriteBeerToRemove = favoriteBeers.random()

            val removeResult = repository.removeFavoriteBeer(favoriteBeerToRemove)
            Assert.assertTrue(removeResult.isSuccess)

            val getWithoutRemovedResult = repository.getFavoriteBeers()
            Assert.assertTrue(getWithoutRemovedResult.isSuccess)

            val gotBeers = getWithoutRemovedResult.getOrThrow()

            Assert.assertEquals(favoriteBeers.size - 1, gotBeers.size)
            Assert.assertFalse(gotBeers.contains(favoriteBeerToRemove))
        }
    }

    @Test
    fun isFavorite() {
        runTest {
            val favoriteBeer = Beer(
                id = 100500,
                name = "Beer #100500",
                abv = Random.nextFloat(),
                ebc = Random.nextFloat(),
                ibu = Random.nextFloat(),
                isFavorite = true
            )
            val notFavoriteBeer = Beer(
                id = 31337,
                name = "Beer #31337",
                abv = Random.nextFloat(),
                ebc = Random.nextFloat(),
                ibu = Random.nextFloat(),
                isFavorite = true
            )

            val addResult = repository.addFavoriteBeer(favoriteBeer)
            Assert.assertTrue(addResult.isSuccess)

            val favoriteResult = repository.isFavorite(favoriteBeer)
            Assert.assertTrue(favoriteResult.isSuccess)
            Assert.assertTrue(favoriteResult.getOrThrow())

            val notFavoriteResult = repository.isFavorite(notFavoriteBeer)
            Assert.assertTrue(notFavoriteResult.isSuccess)
            Assert.assertFalse(notFavoriteResult.getOrThrow())
        }
    }
}
