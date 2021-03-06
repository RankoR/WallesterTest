package page.smirnov.wallester.core_persistence.data.repository

import android.content.Context
import android.database.Cursor
import androidx.core.content.contentValuesOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import page.smirnov.wallester.core.util.withContextCatching
import page.smirnov.wallester.core_persistence.data.db.FavoritesContract
import page.smirnov.wallester.core_persistence.data.db.FavoritesDbHelper
import page.smirnov.wallester.core_persistence.data.model.Beer

/**
 * This is something like an DI Singleton implementation
 * Normally we should use @Singleton from Dagger/Hilt, but we can't use it
 * This repository should exist only in a single instance, as it has a changes flow
 */
object FavoritesRepositoryHolder {

    lateinit var favoritesRepository: FavoritesRepository
        private set

    fun initialize(context: Context) {
        if (::favoritesRepository.isInitialized) {
            throw IllegalStateException("Already initialized")
        }

        val dbHelper = FavoritesDbHelper(context)
        favoritesRepository = FavoritesRepositoryImpl(
            favoritesDbHelper = dbHelper,
            dispatcher = Dispatchers.IO.limitedParallelism(1) // Limit because SQLite doesn't support multithreading
        )
    }
}

interface FavoritesRepository {
    val changesFlow: Flow<Unit> // Fires when favorites are changed

    suspend fun getFavoriteBeers(): Result<List<Beer>>
    suspend fun isFavorite(beer: Beer): Result<Boolean>
    suspend fun addFavoriteBeer(beer: Beer): Result<Unit>
    suspend fun removeFavoriteBeer(beer: Beer): Result<Unit>
}

class FavoritesRepositoryImpl(
    private val favoritesDbHelper: FavoritesDbHelper, // Should be provided with DI
    private val dispatcher: CoroutineDispatcher // Should be provided with DI
) : FavoritesRepository {

    override val changesFlow: MutableSharedFlow<Unit> = MutableSharedFlow(replay = 1)

    override suspend fun getFavoriteBeers(): Result<List<Beer>> {
        return withContextCatching(dispatcher) {
            favoritesDbHelper.readableDatabase.use { db ->
                db.query(
                    FavoritesContract.FavoriteBeer.TABLE_NAME,
                    FavoritesContract.FavoriteBeer.selectionColumns,
                    null,
                    null,
                    null,
                    null,
                    null
                ).use { cursor ->
                    cursor.toList {
                        Beer(
                            id = getLong(getColumnIndexOrThrow(FavoritesContract.FavoriteBeer.COLUMN_NAME_BEER_ID)),
                            name = getString(getColumnIndexOrThrow(FavoritesContract.FavoriteBeer.COLUMN_NAME_BEER_NAME)),
                            abv = getFloat(getColumnIndexOrThrow(FavoritesContract.FavoriteBeer.COLUMN_NAME_ABV)),
                            ebc = getFloat(getColumnIndexOrThrow(FavoritesContract.FavoriteBeer.COLUMN_NAME_EBC)),
                            ibu = getFloat(getColumnIndexOrThrow(FavoritesContract.FavoriteBeer.COLUMN_NAME_IBU)),
                            isFavorite = true
                        )
                    }
                }
            }
        }
    }

    override suspend fun isFavorite(beer: Beer): Result<Boolean> {
        return withContextCatching(dispatcher) {
            favoritesDbHelper.readableDatabase.use { db ->
                db.rawQuery(
                    String.format(FavoritesContract.FavoriteBeer.EXISTS_STATEMENT_FORMAT, beer.id),
                    null
                ).use { cursor ->
                    if (cursor.moveToFirst()) {
                        cursor.getInt(0) == 1
                    } else {
                        false
                    }
                }
            }
        }
    }

    override suspend fun addFavoriteBeer(beer: Beer): Result<Unit> {
        return withContextCatching(dispatcher) {
            favoritesDbHelper.writableDatabase.use { db ->
                val contentValues = contentValuesOf(
                    FavoritesContract.FavoriteBeer.COLUMN_NAME_BEER_ID to beer.id,
                    FavoritesContract.FavoriteBeer.COLUMN_NAME_BEER_NAME to beer.name,
                    FavoritesContract.FavoriteBeer.COLUMN_NAME_ABV to beer.abv,
                    FavoritesContract.FavoriteBeer.COLUMN_NAME_IBU to beer.ibu,
                    FavoritesContract.FavoriteBeer.COLUMN_NAME_EBC to beer.ebc,
                )

                db.insert(FavoritesContract.FavoriteBeer.TABLE_NAME, null, contentValues)
            }

            notifyFavoritesChanged()
        }
    }

    override suspend fun removeFavoriteBeer(beer: Beer): Result<Unit> {
        return withContextCatching(dispatcher) {
            favoritesDbHelper.writableDatabase.use { db ->
                db.delete(
                    FavoritesContract.FavoriteBeer.TABLE_NAME,
                    "${FavoritesContract.FavoriteBeer.COLUMN_NAME_BEER_ID} = ?",
                    arrayOf(beer.id.toString())
                )
            }

            notifyFavoritesChanged()
        }
    }

    private suspend fun notifyFavoritesChanged() {
        changesFlow.emit(Unit)
    }

    private inline fun <T> Cursor.toList(crossinline mappingFunction: Cursor.() -> T): List<T> {
        return generateSequence { if (moveToNext()) this else null }
            .map { it.mappingFunction() }
            .toList()
    }
}
